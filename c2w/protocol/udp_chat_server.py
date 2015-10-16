# -*- coding: utf-8 -*-
from twisted.internet.protocol import DatagramProtocol
from c2w.main.lossy_transport import LossyTransport
from twisted.internet import reactor
import logging
import time
import struct 
import ctypes
from twisted.internet import reactor
from c2w.main.constants import ROOM_IDS
from c2w.main.user import c2wUserStore
logging.basicConfig()
moduleLogger = logging.getLogger('c2w.protocol.udp_chat_server_protocol')


class c2wUdpChatServerProtocol(DatagramProtocol):

    def __init__(self, serverProxy, lossPr):
        """
        :param serverProxy: The serverProxy, which the protocol must use
            to interact with the user and movie store (i.e., the list of users
            and movies) in the server.
        :param lossPr: The packet loss probability for outgoing packets.  Do
            not modify this value!

        Class implementing the UDP version of the client protocol.

        .. note::
            You must write the implementation of this class.

        Each instance must have at least the following attribute:

        .. attribute:: serverProxy

            The serverProxy, which the protocol must use
            to interact with the user and movie store in the server.

        .. attribute:: lossPr

            The packet loss probability for outgoing packets.  Do
            not modify this value!  (It is used by startProtocol.)

        .. note::
            You must add attributes and methods to this class in order
            to have a worclass c2w.main.user.c2wUserStoreking and complete implementation of the c2w
            protocol.
        """
        self.serverProxy = serverProxy
        self.lossPr = lossPr
        self.nbtime = 0                         #### nbtime is used to know how many times we sent the packet
        self.obj={}
        self.sequence_numbers={}
        self.userNames=[]
        self.userIds=[]
        self.useriddestid={}                    #### useriddestid is used to know the location of the client 
      
        
    def startProtocol(self):
        """
        DO NOT MODIFY THE FIRST TWO LINES OF THIS METHOD!!
        """
        self.transport = LossyTransport(self.transport, self.lossPr)
        DatagramProtocol.transport = self.transport

   

###########################################################################################################################################################
    
  ################################################################## DATAGRAM RECEIVED ############################################################### 

    def datagramReceived(self, buf, (host, port)):
        """
        :param string datagram: the payload of the UDP packet.
        :param host: the IP address of the source.
        :param port: the source port.

        Called **by Twisted** when the server has received a UDP
        packet.
        """
        
        adress=(host, port)
        header=struct.unpack_from('>B',buf[0])[0]
        sequence_number=struct.unpack_from('>B',buf[1])[0]
        userId=struct.unpack_from('>B',buf[2])[0]
        destId=struct.unpack_from('>B',buf[3])[0]
        lengthData=struct.unpack_from('>H',buf[4:6])[0]
        data=struct.unpack_from(str(lengthData)+'s',buf[6:])[0]


        userList=self.serverProxy.getUserList()



        ######################################################### Receive login request ######################################################
       
        if(header==3):
           
            if(self.serverProxy.userExists(data)!=True):   #### If the user doesn't exist
                userId=self.serverProxy.addUser(data,ROOM_IDS.MAIN_ROOM,self,adress)        #### Add user in user list
                userList=self.serverProxy.getUserList()            
                self.sequence_numbers[userId]=0
                header=67
                ################ Send Ack login request 
                self.sendAck( header, sequence_number,userId, destId,adress)
                
                movieList=self.serverProxy.getMovieList()                             #### Get movie list
                movieIds=[]
                movieTitles=[]    
                for movie in movieList:
                    movieIds.append(movie.movieId)
                    movieTitles.append(movie.movieTitle)
                ################ Send Movie List
                self.sendMovieList(0, userId, destId,movieTitles,movieIds,movieList,adress)
                self.sequence_numbers[ userId]=self.sequence_numbers[ userId]+1
                 
        
            
            else:                                                               ### USER already exist   
                if (sequence_number!=0):
                    header=123
                    codeError=6
                    user=self.serverProxy.getUserByName(data)
                    ######## Send ERROR message
                    self.sendError(header, 0, user.userId, destId,codeError,adress)  
                    
                else :
                    user=self.serverProxy.getUserByName(data)
                    if (user.userAddress[0]==adress[0] and user.userAddress[1]==adress[1]):     # Receive login request from the same client 
                        header=67
                        user=self.serverProxy.getUserByName(data)
                        ################ Send Ack login request 
                        self.sendAck( header, sequence_number, user.userId, destId,adress)         
                        movieList=self.serverProxy.getMovieList()
                        movieIds=[]
                        movieTitles=[]    
                        for movie in movieList:
                            movieIds.append(movie.movieId)
                            movieTitles.append(movie.movieTitle)
                        ############# Send Movie List       
                        self.sendMovieList(0,  user.userId, destId,movieTitles,movieIds,movieList,adress)
                        self.sequence_numbers[ user.userId]=self.sequence_numbers[ user.userId]+1
               
                    else:                                                   ### Other client try to connect with existed userName
                        header=123
                        codeError=6
                        user=self.serverProxy.getUserByName(data)
                        ######## Send ERROR message
                        self.sendError(header, 0, user.userId, destId,codeError,adress)   


  
                     
        ########################################### Receive forward message main room ########################################################       
        if(header==4):
        
            # send ack to the sender
            header=68
            ####### Send ACK to the message sender
            self.sendAck( header, sequence_number,userId, destId,adress)
            #### forward message main room  
            for user in userList:
                if (user.userChatRoom==ROOM_IDS.MAIN_ROOM and user.userId!=userId):
                    self.forwardMessageMain(self.sequence_numbers[ user.userId],user.userId,destId,data,user.userAddress,userId)
                    self.sequence_numbers[ user.userId]=self.sequence_numbers[ user.userId]+1

        ######################################### Receive forward message movie room #####################################################     
        if(header==5):
            header=69
            ####### Send ACK to the message sender
            self.sendAck( header, sequence_number,userId, destId,adress)
            #### forward message main room
            for user in userList: 
                 if (user.userChatRoom!=ROOM_IDS.MAIN_ROOM):   
                    if (self.useriddestid[user.userId]==destId and user.userId!=userId):           #choose the person in the specific room
                        self.forwardMessageMovie(self.sequence_numbers[ user.userId], user.userId,destId,data,user.userAddress,userId)
                        self.sequence_numbers[ user.userId]=self.sequence_numbers[ user.userId]+1
     

        ########################################  Receive Join movie room  ###########################################################
        if(header==35):
            header=99
            # insert code get the port id and ip adress from room id the client request                
            movieTuple= self.serverProxy.getMovieAddrPortById(destId)
            portMovie= movieTuple[1]    ### Movie Port number 
            addressMovie= movieTuple[0] ### Movie address
            ########## Send  ACK Join movie room
            self.sendAckJoinMovie(header, sequence_number, userId,0,portMovie,addressMovie,adress) 
            movie=self.serverProxy.getMovieById(destId)

            self.serverProxy.startStreamingMovie(movie.movieTitle)          ##### Start video Streaming

            user=self.serverProxy.getUserById(userId)
            
            user.userChatRoom=ROOM_IDS.MOVIE_ROOM

            self.useriddestid[user.userId]=destId
           
            usersChat=[]                                                    #### usersChat is the users in specific movieroom
            for user in userList:
                if(user.userChatRoom!=ROOM_IDS.MAIN_ROOM):                   
                    if (self.useriddestid[user.userId]==destId):                
                        usersChat.append(user)
            userChatIds=[]
            userChatNames=[]    
            for user in usersChat:
                userChatIds.append(user.userId)
                userChatNames.append(user.userName)
            ############# Send userList (main room)  and userChatList (specific movieroom)    
            for user in usersChat:
                self.sendChatUserList(self.sequence_numbers[ user.userId], user.userId, destId,userChatNames,userChatIds,usersChat,user.userAddress)
                self.sequence_numbers[ user.userId]=self.sequence_numbers[ user.userId]+1  
            for user in userList:    
                if (user.userChatRoom==ROOM_IDS.MAIN_ROOM ):
                    self.sendMainRoomUserList(self.sequence_numbers[ user.userId],user.userId,0,self.userNames,self.userIds,userList,user.userAddress)
                    self.sequence_numbers[ user.userId]=self.sequence_numbers[ user.userId]+1      
                
        ############################################# Receive  leave systeme ###################################################
        if (header==60 ): 
            user=self.serverProxy.getUserById(userId)
            userName=user.userName
            self.serverProxy.removeUser(userName)
            header=124
            ##### Send ACK
            self.sendAck( header, sequence_number, userId, destId,adress)
            userList=self.serverProxy.getUserList()
            self.userNames.remove(userName)
            self.userIds.remove(userId)
            ########### Send User List (main room)
            for user in userList:    
                if (user.userChatRoom==ROOM_IDS.MAIN_ROOM ):
                    self.sendMainRoomUserList(self.sequence_numbers[ user.userId], user.userId, destId,self.userNames,self.userIds,userList,user.userAddress)
                    
                    
        ############################################# leave movie Room #############################################################   
        if (header==33): 
            user=self.serverProxy.getUserById(userId)
            userName=user.userName
            header=97
            ############## Send ACK
            self.sendAck( header, sequence_number, userId, destId,adress)

            #movie=self.serverProxy.getMovieById(self.useriddestid)
            #self.serverProxy.stopStreamingMovie(movie.movieTitle)                   ####### Stop video streaming
            user.userChatRoom=ROOM_IDS.MAIN_ROOM
            idMovieroom=self.useriddestid[user.userId]
            userList=self.serverProxy.getUserList()
            self.useriddestid[user.userId]=destId
            userNamesMovie=[]
            userIdsMovie=[]
            usersChat=[]                                                            ## userList in specific movie room
            
            ########## send userList (main room) and userChat List (specific movie room)            
            for user in userList:
                if (user.userChatRoom!=ROOM_IDS.MAIN_ROOM ):
                    if (self.useriddestid[user.userId]==idMovieroom):
                        userNamesMovie.append(user.userName)
                        userIdsMovie.append(user.userId)
                        usersChat.append(user)
            for user in userList:    
                if (user.userChatRoom==ROOM_IDS.MAIN_ROOM ):
                    self.sendMainRoomUserList(self.sequence_numbers[ user.userId], user.userId, destId,self.userNames,self.userIds,userList,user.userAddress)
                elif (self.useriddestid[user.userId]==idMovieroom):
                    self.sendChatUserList(self.sequence_numbers[user.userId], user.userId, idMovieroom,userNamesMovie,userIdsMovie,usersChat,user.userAddress)     
        


        
        ##############################################################################################################################
                                                         
                                                  ######################## ACK ###################
            
       ##########################################################  Receive Ack movieList ############################################## #                      
        if(header==79):        
            print "ACK_MovieList received"
            self.obj[userId].cancel()
            self.nbtime = 0
            #user List
            self.userIds=[]
            self.userNames=[]

            for user in userList:
                self.userIds.append(user.userId)
                self.userNames.append(user.userName)
            
            ########### Send User List (main room)
            for user in userList:
                if (user.userChatRoom==ROOM_IDS.MAIN_ROOM):
                    self.sendMainRoomUserList(self.sequence_numbers[ user.userId], user.userId, destId,self.userNames,self.userIds,userList,user.userAddress)
                    self.sequence_numbers[user.userId]=self.sequence_numbers[user.userId]+1   
        
        ###################################################### Receive Ack userList ###########################################################
        if(header==87):        
            print "userList ACK received"
            self.obj[userId].cancel()
            self.nbtime = 0
             # Ack message main room                      
       
        ############################################### Receive Ack message main room  ############################################################      
        if(header==112):        
            print "message main room ACK received"
            self.obj[userId].cancel()
            self.nbtime = 0
        
        ################################################ Receive Ack message MOVIE room ############################################## #                        
            
        if(header==113):        
            print "message movie room ACK received"
            self.obj[userId].cancel()
            self.nbtime = 0
       

        pass



##################################################################### FUNCTIONS ###################################################################
 
            
    
                               ######################## Send Request ###########################


    def sendRequest(self, buf, adress,userId):
        """
        :param line: the text of the question from the user interface
        :type line: string

        This function must send the request to the server.
        """
        self.transport.write(buf,adress)
        
        #time out
        self.nbtime=self.nbtime+1
        if(self.nbtime<=3):
            print "send packet"+str (self.nbtime)
            self.obj[userId]=reactor.callLater(3,self.sendRequest, buf, adress,userId)
            if(self.nbtime==3):
                print 'end'
                self.obj[userId].cancel()
                self.nbtime=0       
            
                            ##################### send ACK ###################################

    def sendAck(self, header, sequence_number, userId, destId,adress):
        buf=ctypes.create_string_buffer(6)
        struct.pack_into('>BBBBH',buf,0,header,sequence_number,userId,destId,0)
        self.transport.write(buf,adress)        
        
                            ##################### send Error message ###################################

    def sendError(self, header, sequence_number, userId, destId,codeError,adress):
        buf=ctypes.create_string_buffer(7)
        struct.pack_into('>BBBBHB',buf,0,header,sequence_number,userId,destId,1,codeError)
        self.transport.write(buf,adress)        
    
                            ##################### send User List (main room) ###################################
    def sendMainRoomUserList(self,sequence_number, userId, destId,userNames,userIds,userList,adress):
        header=20
        numberUsers=len(userIds)
        lengtNames=0
        for userName in userNames:
            lengtNames=len(userName)+lengtNames
        dataLength=lengtNames+3*numberUsers
        buf1=ctypes.create_string_buffer(6+dataLength)

        struct.pack_into('>BBBBH',buf1,0,header,sequence_number,userId,destId,dataLength)
        longueur=6
        
        for user in userList:
            availibility= user.userChatRoom
            if (availibility==ROOM_IDS.MAIN_ROOM):
                availibility=128
            else:
                availibility=0
            
            struct.pack_into('>BBB'+str(len(user.userName))+'s',buf1,longueur,len(user.userName),user.userId,availibility,user.userName)
            longueur=3+len(user.userName)+longueur
        #self.transport.write(buf1,adress)
        self.sendRequest(buf1,adress,userId)

            ##################### send Movie List ###################################
    def sendMovieList(self,sequence_number, userId, destId,movieTitles,movieIds,movieList,adress):
        header=15
        numberMovies=len(movieIds)
        lengtNames=0
        for movieTitle in movieTitles:
            lengtNames=len(movieTitle)+lengtNames
        dataLength=lengtNames+2*numberMovies

        buf1=ctypes.create_string_buffer(6+dataLength)

        struct.pack_into('>BBBBH',buf1,0,header,sequence_number,userId,destId,dataLength)

        longueur=6 
        
        
        for movie in movieList:
   
            struct.pack_into('>BB'+str(len(movie.movieTitle))+'s',buf1,longueur,len(movie.movieTitle),movie.movieId,movie.movieTitle)
            longueur=2+len(movie.movieTitle)+longueur

        self.sendRequest(buf1,adress,userId)


            ##################### send forward message main room ###################################
    def forwardMessageMain(self,sequence_number,userId,destId,data,adress,userId_sender):
        header=48
       
        dataLength=len(data)+1

        buf1=ctypes.create_string_buffer(6+dataLength)
        userId_sender_data=str(userId_sender)+data

        struct.pack_into('>BBBBH'+str(dataLength)+'s',buf1,0,header,sequence_number,userId,destId,dataLength,userId_sender_data)

       
        self.sendRequest(buf1,adress,userId)


            ############################ send forward message movie room ###################################

    def forwardMessageMovie(self,sequence_number,userId,destId,data,adress,userId_sender):
        header=49
       
        dataLength=len(data)+1

        buf1=ctypes.create_string_buffer(6+dataLength)
        userId_sender_data=str(userId_sender)+data

        struct.pack_into('>BBBBH'+str(dataLength)+'s',buf1,0,header,sequence_number,userId,destId,dataLength,userId_sender_data)

       
        self.sendRequest(buf1,adress,userId)


                ##################### send ACK Join Movie Room ###################################
    def sendAckJoinMovie(self, header, sequence_number, userId, destId,data1,data2,adress):
        # edit to send the data ip and port of movie request   
        buf=ctypes.create_string_buffer(12)
        part = data2.split('.')
        ip1= int(part[0])
        ip2= int(part[1])
        ip3= int(part[2])
        ip4= int(part[3])
        struct.pack_into('>BBBBHHBBBB',buf,0,header,sequence_number,userId,destId,6,data1,ip1,ip2,ip3,ip4)
        self.transport.write(buf,adress)        
        
                #################### send User Chat List (specific movie room) ################################### 
    def sendChatUserList(self,sequence_number, userId, destId,userNames,userIds,usersChat,adress):    
        header=21
        numberUsers=len(userIds)
        lengtNames=0
        for userName in userNames:
            lengtNames=len(userName)+lengtNames
        dataLength=lengtNames+3*numberUsers
        buf1=ctypes.create_string_buffer(6+dataLength)

        struct.pack_into('>BBBBH',buf1,0,header,sequence_number,userId,destId,dataLength)
        longueur=6
        
        for user in usersChat:
            struct.pack_into('>BBB'+str(len(user.userName))+'s',buf1,longueur,len(user.userName),user.userId,0,user.userName)
            longueur=3+len(user.userName)+longueur
        self.sendRequest(buf1,adress,userId)






















