# -*- coding: utf-8 -*-
from twisted.internet.protocol import Protocol
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


class c2wTcpChatServerProtocol(Protocol):

    def __init__(self, serverProxy, clientAddress, clientPort):
        """
        :param serverProxy: The serverProxy, which the protocol must use
            to interact with the user and movie store (i.e., the list of users
            and movies) in the server.
        :param clientAddress: The IP address (or the name) of the c2w server,
            given by the user.
        :param clientPort: The port number used by the c2w server,
            given by the user.

        Class implementing the UDP version of the client protocol.

        .. note::
            You must write the implementation of this class.

        Each instance must have at least the following attribute:

        .. attribute:: serverProxy

            The serverProxy, which the protocol must use
            to interact with the user and movie store in the server.

        .. attribute:: clientAddress

            The IP address (or the name) of the c2w server.

        .. attribute:: clientPort

            The port number used by the c2w server.

        .. note::
            You must add attributes and methods to this class in order
            to have a working and complete implementation of the c2w
            protocol.

        .. note::
            The IP address and port number of the client are provided
            only for the sake of completeness, you do not need to use
            them, as a TCP connection is already associated with only
            one client.
        """
        self.clientAddress = clientAddress
        self.clientPort = clientPort
        self.serverProxy = serverProxy
        self.bufStore=""                                ### BufStore is used to build the frame (framing)           
        self.userNames=[]
        self.userIds=[]
        self.useriddestid=-1                           #### useriddestid is used to know the location of the client 
        self.sequence_number=0
       



###########################################################################################################################################################
    
  ############################################################# DATA RECEIVED ############################################################################
    def dataReceived(self, buf):
        """
        :param string datagram: the payload of the UDP packet.
        :param host: the IP address of the source.
        :param port: the source port.

        Called **by Twisted** when the server has received a UDP
        packet.
        """
        buf1=self.buildFrame(buf)


        if(buf1!=None):
            
            header=struct.unpack_from('>B',buf1[0])[0]
            sequence_number=struct.unpack_from('>B',buf1[1])[0]
            userId=struct.unpack_from('>B',buf1[2])[0]
            destId=struct.unpack_from('>B',buf1[3])[0]
            lengthData=struct.unpack_from('>H',buf1[4:6])[0]
            data=struct.unpack_from(str(lengthData)+'s',buf1[6:])[0]
            userList=self.serverProxy.getUserList()


            #################################### Receive login request ######################################################
       
            if(header==3):
            
                if(self.serverProxy.userExists(data)!=True):   #### If the user doesn't exist
                    userId=self.serverProxy.addUser(data,ROOM_IDS.MAIN_ROOM,self)        #### Add user in user list
                    userList=self.serverProxy.getUserList()
                    self.sequence_number=0
                else :
                    header=123
                    codeError=6
                    user=self.serverProxy.getUserByName(data)
                        ######## Send ERROR message
                    self.sendError(header, 0, user.userId, destId,codeError)   
               
                
                if (sequence_number==0):
                    header=67
                    user=self.serverProxy.getUserByName(data)
                     ################ Send Ack login request 
                    self.sendAck( header, sequence_number, user.userId, destId)         
                    movieList=self.serverProxy.getMovieList()
                    movieIds=[]
                    movieTitles=[]    
                    for movie in movieList:
                        movieIds.append(movie.movieId)
                        movieTitles.append(movie.movieTitle)
                       ############# Send Movie List       
                    self.sendMovieList(0,  user.userId, destId,movieTitles,movieIds,movieList)
                    self.sequence_number=self.sequence_number+1
                    
                else :
                    header=123
                    codeError=6
                    user=self.serverProxy.getUserByName(data)
                        ######## Send ERROR message
                    self.sendError(header, 0, user.userId, destId,codeError)   
               
          



            ########################################### Receive forward message main room ########################################################       
            if(header==4):
                header=68
               ####### Send ACK to the message sender 
                self.sendAck( header, sequence_number,userId, destId)
               #### forward message main room                
                for user in userList:
                    if (user.userChatRoom==ROOM_IDS.MAIN_ROOM and user.userChatInstance!=self):          
                        user.userChatInstance.forwardMessageMain(user.userChatInstance.sequence_number,user.userId,destId,data,userId) 
                        user.userChatInstance.sequence_number=user.userChatInstance.sequence_number+1
         
            ######################################### Receive forward message movie room #####################################################  
            if(header==5):
                header=69
                ####### Send ACK to the message sender 
                self.sendAck( header, sequence_number,userId, destId)
                #### forward message movie room
                for user in userList:
                    if (user.userChatInstance.useriddestid==destId and user.userChatInstance!=self):          
                        user.userChatInstance.forwardMessageMovie(user.userChatInstance.sequence_number, user.userId,destId,data,userId)  
                        user.userChatInstance.sequence_number=user.userChatInstance.sequence_number+1
            
            ########################################  Receive Join movie room  ###########################################################
            if(header==35):
                header=99
                movieTuple= self.serverProxy.getMovieAddrPortById(destId)
                portMovie= movieTuple[1]    ### Movie Port number 
                addressMovie= movieTuple[0] ### Movie address
                ########## Send  ACK Join movie room
                self.sendAckJoinMovie(header, sequence_number, userId,0,portMovie,addressMovie) 

                movie=self.serverProxy.getMovieById(destId)
                
                self.serverProxy.startStreamingMovie(movie.movieTitle)  ##### Start video Streaming
                
                user=self.serverProxy.getUserById(userId)
                user.userChatRoom=ROOM_IDS.MOVIE_ROOM
                self.useriddestid=destId
                usersChat=[]                                            #### usersChat is the users in specific movieroom
                for user in userList:
                    if (user.userChatInstance.useriddestid==destId):                
                        usersChat.append(user)
                userChatIds=[]
                userChatNames=[]    
                for user in usersChat:
                    userChatIds.append(user.userId)
                    userChatNames.append(user.userName)
               
                ############# Send userList (main room)  and userChatList (specific movieroom)
                for user in usersChat:
                    user.userChatInstance.sendChatUserList(user.userChatInstance.sequence_number, user.userId, destId,userChatNames,userChatIds,usersChat)
                    user.userChatInstance.sequence_number=user.userChatInstance.sequence_number+1  
                for user in userList:    
                    if (user.userChatRoom==ROOM_IDS.MAIN_ROOM ):
                        user.userChatInstance.sendMainRoomUserList(user.userChatInstance.sequence_number,user.userId,0,self.userNames,self.userIds,userList)
                        user.userChatInstance.sequence_number=user.userChatInstance.sequence_number+1      
                
            ############################################# Receive  leave systeme ###################################################
            if (header==60 ): 
                user=self.serverProxy.getUserById(userId)
                userName=user.userName
                self.serverProxy.removeUser(userName)
                header=124
                ##### Send ACK
                self.sendAck( header, sequence_number, userId, destId)
                userList=self.serverProxy.getUserList()
                self.userNames.remove(userName)
                self.userIds.remove(userId)
                ########### Send User List (main room)
                for user in userList:    
                    if (user.userChatRoom==ROOM_IDS.MAIN_ROOM ):
                        user.userChatInstance.sendMainRoomUserList(user.userChatInstance.sequence_number, user.userId, destId,self.userNames,self.userIds,userList)
                        user.userChatInstance.sequence_number=user.userChatInstance.sequence_number+1  
                    
            ############################################# leave movie Room ###############################################
            if (header==33): 
                user=self.serverProxy.getUserById(userId)
                
                userName=user.userName
                header=97
                ############## Send ACK
                self.sendAck( header, sequence_number, userId, destId)
                
                movie=self.serverProxy.getMovieById(self.useriddestid)
                               
                self.serverProxy.stopStreamingMovie(movie.movieTitle)  ####### Stop video streaming  
                user.userChatRoom=ROOM_IDS.MAIN_ROOM
                idMovieroom=self.useriddestid
                userList=self.serverProxy.getUserList()
                self.useriddestid=destId
                userNamesMovie=[]
                userIdsMovie=[]
                usersChat=[]  ## userList in specific movie room  
                for user in userList:
                    if (user.userChatInstance.useriddestid==idMovieroom):
                        userNamesMovie.append(user.userName)
                        userIdsMovie.append(user.userId)
                        usersChat.append(user)
                ########## send userList (main room) and userChat List (specific movie room) 
                for user in userList:    
                    if (user.userChatRoom==ROOM_IDS.MAIN_ROOM ):
                        user.userChatInstance.sendMainRoomUserList(user.userChatInstance.sequence_number, user.userId, destId,self.userNames,self.userIds,userList)
                        user.userChatInstance.sequence_number=user.userChatInstance.sequence_number+1
                    if (user.userChatInstance.useriddestid==idMovieroom):
                        user.userChatInstance.sendChatUserList(user.userChatInstance.sequence_number, user.userId, idMovieroom,userNamesMovie,userIdsMovie,usersChat)     
                        user.userChatInstance.sequence_number=user.userChatInstance.sequence_number+1
            
            ####################################################################################################################################
                                                         
                                                     ######################## ACK ###################
            
                        ########################################  Receive Ack movieList #########################################                        
            if(header==79):        
                print "ACK_MovieList received"
                for user in userList:
                    self.userIds.append(user.userId)
                    self.userNames.append(user.userName)
                for user in userList:
                    if(user.userChatInstance!=self):
                        user.userChatInstance.userNames=self.userNames
                        user.userChatInstance.userIds=self.userIds
                ########### Send User List (main room)
                for user in userList:
                    if (user.userChatRoom==ROOM_IDS.MAIN_ROOM):
                        user.userChatInstance.sendMainRoomUserList(user.userChatInstance.sequence_number, user.userId, destId,self.userNames,self.userIds,userList)
                        user.userChatInstance.sequence_number=user.userChatInstance.sequence_number+1   
            
                 ######################################## Receive Ack userList ############################################                        
            if(header==87):        
                print "userList ACK received"
            ######################################## Receive Ack message main room #############################################       
            if(header==112):        
                print "message main room ACK received"
             
            ######################################## Receive Ack message movie room  #############################################
                                    
            if(header==113):        
                print "message movie room ACK received"
    
          

            pass

 







          ######################################################### FUNCTIONS #######################################################
 
            
    
                                         ######################## Send Request ###########################

    def sendRequest(self, buf):
        """
        :param line: the text of the question from the user interface
        :type line: string

        This function must send the request to the server.
        """
        self.transport.write(buf.raw)
      

                                       ############################# send ACK ###################################

    def sendAck(self, header, sequence_number, userId, destId):
        buf=ctypes.create_string_buffer(6)
        struct.pack_into('>BBBBH',buf,0,header,sequence_number,userId,destId,0)
        self.transport.write(buf.raw)
         
        
                                ################################ send Error message ###################################

    def sendError(self, header, sequence_number, userId, destId,codeError):
        buf=ctypes.create_string_buffer(7)
        struct.pack_into('>BBBBHB',buf,0,header,sequence_number,userId,destId,1,codeError)
        self.transport.write(buf.raw)        
                    
                                ##################### send User List (main room) ###################################
    
    def sendMainRoomUserList(self,sequence_number, userId, destId,userNames,userIds,userList):
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
            
        self.sendRequest(buf1)

                               ##################### send Movie List ###################################
    def sendMovieList(self,sequence_number, userId, destId,movieTitles,movieIds,movieList):
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

        self.sendRequest(buf1)

                             ##################### send forward message main room ###################################
    def forwardMessageMain(self,sequence_number,userId,destId,data,userId_sender):
        header=48
       
        dataLength=len(data)+1

        buf1=ctypes.create_string_buffer(6+dataLength)
        userId_sender_data=str(userId_sender)+data
        struct.pack_into('>BBBBH'+str(dataLength)+'s',buf1,0,header,sequence_number,userId,destId,dataLength,userId_sender_data)

       
        self.sendRequest(buf1)
    
                             ############################ send forward message movie room ###################################
    def forwardMessageMovie(self,sequence_number,userId,destId,data,userId_sender):
        header=49
       
        dataLength=len(data)+1

        buf1=ctypes.create_string_buffer(6+dataLength)
        userId_sender_data=str(userId_sender)+data
        struct.pack_into('>BBBBH'+str(dataLength)+'s',buf1,0,header,sequence_number,userId,destId,dataLength,userId_sender_data)

       
        self.sendRequest(buf1)



                            ##################### send ACK Join Movie Room ###################################
    def sendAckJoinMovie(self, header, sequence_number, userId, destId,portMovie,adressMovie):
        buf=ctypes.create_string_buffer(12)
        part = adressMovie.split('.')
        ip1= int(part[0])
        ip2= int(part[1])
        ip3= int(part[2])
        ip4= int(part[3])
        struct.pack_into('>BBBBHHBBBB',buf,0,header,sequence_number,userId,destId,6,portMovie,ip1,ip2,ip3,ip4)
        self.transport.write(buf.raw)              
 
                            ##################### send User Chat List (specific movie room) ###################################       
    def sendChatUserList(self,sequence_number, userId, destId,userNames,userIds,usersChat):   
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
            
        self.sendRequest(buf1)

                           ##################### Build the frame (framing) ###################################
    def buildFrame(self,buf):
        self.bufStore=self.bufStore+buf
        
        if(len(self.bufStore)<6):
            return None
        else:
            
            dataLength=struct.unpack_from('>H', self.bufStore[4:6])[0]
            if(dataLength==len(self.bufStore)-6):
                buf1=self.bufStore
                self.bufStore=""
                return buf1
                
            elif(dataLength<len(self.bufStore)-6):
                
                buf1=self.bufStore[0:dataLength+6]
                self.bufStore=self.bufStore[dataLength+6:]

                return buf1
            else: 
                return None
                
                                 
            
                    
        
   



        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
	


