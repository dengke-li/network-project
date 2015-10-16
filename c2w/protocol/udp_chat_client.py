# -*- coding: utf-8 -*-
from twisted.internet.protocol import DatagramProtocol
from twisted.internet import reactor
from c2w.main.lossy_transport import LossyTransport
import logging
import struct 
import ctypes
from c2w.main.client_model import c2wClientModel
from c2w.main.constants import ROOM_IDS


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


logging.basicConfig()
moduleLogger = logging.getLogger('c2w.protocol.udp_chat_client_protocol')


class c2wUdpChatClientProtocol(DatagramProtocol):
    

    def __init__(self, serverAddress, serverPort, clientProxy, lossPr):
        """
        :param serverAddress: The IP address (or the name) of the c2w server,
            given by the user.
        :param serverPort: The port number used by the c2w server,
            given by the user.
        :param clientProxy: The clientProxy, which the protocol must use
            to interact with the Graphical User Interface.

        Class implementing the UDP version of the client protocol.

        .. note::
            You must write the implementation of this class.

        Each instance must have at least the following attributes:

        .. attribute:: serverAddress

            The IP address (or the name) of the c2w server.

        .. attribute:: serverPort

            The port number used by the c2w server.

        .. attribute:: clientProxy

            The clientProxy, which the protocol must use
            to interact with the Graphical User Interface.

        .. attribute:: lossPr

            The packet loss probability for outgoing packets.  Do
            not modify this value!  (It is used by startProtocol.)

        .. note::
            You must add attributes and methods to this class in order
            to have a working and complete implementation of the c2w
            protocol.
        """

        self.serverAddress = serverAddress
        self.serverPort = serverPort
        self.clientProxy = clientProxy
        self.lossPr = lossPr
        self.nbtime = 0
        self.movielist=''
        self.USERID=0
        self.userName=''
        self.roomName=''
        self.flag=0
        self.obj=None
        self.flaguser=0
        self.flagmovie=0
        self.clientsequencenb=0
        self.userListresult=[]
        self.movieListresult=[]
        self.movieListstock=[]
        self.userListstock=[]
        self.location=-1
        self.roomId=0
        self.message=''
        self.count=0
        self.prefrg=0
        self.moviefrg=''
        self.movielengthfrg=0
        self.userlengthfrg=0
        self.previousserverseqnb=-1
       
        
    def startProtocol(self):
        """
        DO NOT MODIFY THE FIRST TWO LINES OF THIS METHOD!!
        """
        self.transport = LossyTransport(self.transport, self.lossPr)
        DatagramProtocol.transport = self.transport
        
    
    def sendRequest(self, buf, adress):
        """
        :param line: the text of the question from the user interface
        :type line: string

        This function must send the request to the server.
        """
        self.transport.write(buf,adress)
     #### send login request
    def sendLoginRequestOIE(self, userName):
        """
        :param string userName: The user name that the user has typed.

        The controller calls this function as soon as the user clicks on
        the login button.
        """
       
        moduleLogger.debug('loginRequest called with username=%s', userName)
        
        self.userName=userName
        lenUserName=len(userName)
        
        s=struct.Struct('!BBBBH'+str(lenUserName)+'s')
        packdata=s.pack(*(3,0,0,0,lenUserName, userName))
        adress=(self.serverAddress,self.serverPort)
        
        self.sendRequest(packdata,adress)
        self.nbtime=self.nbtime+1
      
    ####Set the time out    
        if(self.nbtime<=3):
            
            self.timeout(userName,0)
        if(self.nbtime==4):
            self.nbtime=0
            
    ### Send movie list ACK
                
    def sendMovieListAck(self,sequencenb,userid):
        #moduleLogger.debug('loginRequest called with username=%s', )
        buf=ctypes.create_string_buffer(6)
        struct.pack_into('>BBBBH',buf,0,79,sequencenb,userid,0,0)
        adress=(self.serverAddress,self.serverPort)
        self.sendRequest(buf,adress)
    ### Send user list ACK
    
    def sendUserListAck(self,sequencenb,userid):
        #moduleLogger.debug('loginRequest called with username=%s', )
        buf=ctypes.create_string_buffer(6)
        struct.pack_into('!BBBBH',buf,0,87,sequencenb,userid,0,0)
        adress=(self.serverAddress,self.serverPort)
        self.sendRequest(buf,adress)
    ### Send message ACK
    
    def sendmessageAck(self,serversequencenb,userid,roomtype):
        buf=ctypes.create_string_buffer(6)
        if(roomtype==0):
            struct.pack_into('!BBBBH',buf,0,112,serversequencenb,userid,0,0)
           
        if(roomtype==1):
            struct.pack_into('!BBBBH',buf,0,113,serversequencenb,userid,0,0)
    
        adress=(self.serverAddress,self.serverPort)
        self.sendRequest(buf,adress)
        
        
     ###### Send chat message    
        
    def sendChatMessageOIE(self, message):
        """
        :param message: The text of the chat message.
        :type message: string

        Called **by the controller**  when the user has decided to send
        a chat message

        .. note::
           This is the only function handling chat messages, irrespective
           of the room where the user is.  Therefore it is up to the
           c2wChatClientProctocol or to the server to make sure that this
           message is handled properly, i.e., it is shown only by the
           client(s) who are in the same room.
           
        """
        length=len(message)
        if(length<140):
            self.message=message
           
            buf=ctypes.create_string_buffer(6+length)
            if(self.location==0):                                                 #### location=0 mean user is being mainroom, and send chat message
                
                struct.pack_into('!BBBBH'+str(length)+'s',buf,0,4,self.clientsequencenb,self.USERID,0,length,message) 
                adress=(self.serverAddress,self.serverPort)
                self.sendRequest(buf,adress)
            if(self.location==1):                                                 #### location=1 mean user is already in mainroom, and send chat message
               
                struct.pack_into('!BBBBH'+str(length)+'s',buf,0,5,self.clientsequencenb,self.USERID,self.roomId,length,message) 
                adress=(self.serverAddress,self.serverPort)
                self.sendRequest(buf,adress)
            self.nbtime=self.nbtime+1
             #### set the timeout
            if(self.nbtime<=3):
              
                self.timeout(message,2)
            if(self.nbtime==4):
                self.nbtime=0
        

    def sendJoinRoomRequestOIE(self, roomName):   
        """
        :param roomName: The room name (or movie title.)

        Called **by the controller**  when the user
        has clicked on the watch button or the leave button,
        indicating that she/he wants to change room.

        .. warning:
            The controller sets roomName to
            c2w.main.constants.ROOM_IDS.MAIN_ROOM when the user
            wants to go back to the main room.
            
        """
        moduleLogger.debug('loginRequest called with username=%s', )
        
        if(self.location==0):                                              #### location=0  user is being mainroom, and send room request
                   
            for tuple in self.movieListstock:                              ##### take roomid from roomname
                if tuple[0]==roomName:
                    roomid=tuple[3]
          
            self.roomName=roomName
            self.roomId = roomid
    
            buf=ctypes.create_string_buffer(6)
            struct.pack_into('!BBBBH',buf,0,35,self.clientsequencenb,self.USERID,roomid,0) 
            adress=(self.serverAddress,self.serverPort)
            self.sendRequest(buf,adress)
            
            
            
            
        if(self.location==1):                                              #### location=1 mean user is already in mainroom, and send request to leave movingroom
            buf=ctypes.create_string_buffer(6)
            struct.pack_into('!BBBBH',buf,0,33,self.clientsequencenb,self.USERID,0,0)   
            adress=(self.serverAddress,self.serverPort)
         
            self.sendRequest(buf,adress)
        self.nbtime=self.nbtime+1
        if(self.nbtime<=3):
           
            self.timeout(roomName,1)
        if(self.nbtime==4):
            self.nbtime=0
        
        
            
        
        
        
    #### send request to leave out application
    
    def sendLeaveSystemRequestOIE(self):
        """
        Called **by the controller**  when the user
        has clicked on the leave button in the main room.
        """
        pass
        
        
        buf=ctypes.create_string_buffer(6)
        struct.pack_into('!BBBBH',buf,0,60,self.clientsequencenb,self.USERID,0,0)
        adress=(self.serverAddress,self.serverPort)
        
        self.sendRequest(buf,adress)
        self.nbtime=self.nbtime+1
        if(self.nbtime<=3):
          
            self.timeout(0,3)
        if(self.nbtime==4):
            self.nbtime=0
    
        
     #### timeout function
    def timeout(self,data,functionid):
        
       
        
       
        if(self.nbtime==3):
          
            self.obj.cancel()
            if(functionid==0):
                self.clientProxy.connectionRejectedONE('connection failed,please try again')    ##if in case mainroom to movieroom, we think client don't quit
        if(self.flag==0):
           
           
            if(functionid==0):
                self.obj=reactor.callLater(3,self.sendLoginRequestOIE,data)
            if(functionid==1):
                self.obj=reactor.callLater(3,self.sendJoinRoomRequestOIE,data)
            if(functionid==2):
               
                self.obj=reactor.callLater(3,self.sendChatMessageOIE,data)
            if(functionid==3):
                self.obj=reactor.callLater(3,self.sendLeaveSystemRequestOIE)
            
            
    ### data recived
    def datagramReceived(self, buf, (host, port)):
        """
        :param string datagram: the payload of the UDP packet.
        :param host: the IP address of the source.
        :param port: the source port.

        Called **by Twisted** when the client has received a UDP
        packet.
        """

       
        adress=(host, port)
        
       
        
        tuple1=struct.unpack_from('>B',buf[0])
      
        
        frg=self.getbit(tuple1[0],1,1)
        
        ack=self.getbit(tuple1[0],2,2)
        
        mestype=self.getbit(tuple1[0],3,6)
        roomtype=self.getbit(tuple1[0],7,8)
        tuple2=struct.unpack_from('>B',buf[1])
     
        serversequencenb=tuple2[0]	
        tuple3=struct.unpack_from('>B',buf[2])
        userid=tuple3[0]
        tuple4=struct.unpack_from('>B',buf[3])
        destid=tuple4[0]
        tuple5=struct.unpack_from('>H',buf[4:6])
       
        length=tuple5[0]
        if (ack==1 and mestype==8 and self.location==0):         #### use when receiving room request ACK
            port=struct.unpack_from('>H',buf[6:8])[0]
            ip1 = struct.unpack_from('>B',buf[8])[0]
            ip2 = struct.unpack_from('>B',buf[9])[0]
            ip3 = struct.unpack_from('>B',buf[10])[0]
            ip4 = struct.unpack_from('>B',buf[11])[0]
        
            
        else:
            tuple6=struct.unpack_from(str(length)+'s',buf[6:])
            data=tuple6[0]
        
    
        if(ack==1 and mestype==0):  ## login ack
            
            self.flag=1
            #self.timeout(data)
      
            self.nbtime=0
            self.obj.cancel()
            self.USERID=userid
            
            self.flag=0
        if(ack==1 and mestype==1):   ## chat message ack
            
            self.flag=1
            #self.timeout(data)
        
            self.nbtime=0
            self.obj.cancel()
            self.clientsequencenb=self.clientsequencenb+1
          
            self.flag=0
        
        
        if(ack==0 and mestype==12):                             #receive the forward message
          
            self.sendmessageAck(serversequencenb,self.USERID,roomtype)
            #self.count=self.count+1
      
            userid=int(data[0])
           
    
            for user in self.userListstock:
              
                if user[0]==userid:
                
                    userName=user[1]
            
            self.clientProxy.chatMessageReceivedONE(userName, data[1:])
             
        if(ack==1 and mestype==14):   ### error message
            errmes=struct.unpack_from('>B',data[0])[0]
          
            if(errmes==6):
                self.clientProxy.connectionRejectedONE('username not available')
            
            elif(errmes==4):
                self.obj.cancel()

        if(mestype==3 and ack==0):	## receive the movielist given by the server,client send movielistack to server
              
            self.sendMovieListAck(serversequencenb,userid)
                         ### flag indicate that client has already received the movie list
            if(frg==1 and serversequencenb > self.previousserverseqnb):
                self.prefrg=1
                self.moviefrg=self.moviefrg+data
                self.movielengthfrg=self.movielengthfrg+length
                self.previousserverseqnb=serversequencenb
            if (frg==0 and self.prefrg==1 and serversequencenb > self.previousserverseqnb):
                self.moviefrg=self.moviefrg+data
                self.prefrg=0
                self.movielist=self.moviefrg
                self.movielistlength=self.movielengthfrg+length
                self.previousserverseqnb=serversequencenb
                self.flagmovie=1

            if(frg==0 and self.prefrg==0):
                self.movielist=data
                self.movielistlength=length
    
                self.flagmovie=1     
        if(mestype==5 and ack==0 and userid==self.USERID):	##if it is the userlist given by the server,client send userlistack to server(from mainroom or movieroom)             
            
       
            self.sendUserListAck(serversequencenb,userid)
            
                       ##when the user is already in mainroom or movieroom, he receives userlist update
                
            
            if(frg==1 and serversequencenb > self.previousserverseqnb):               ## we use the server's sequence nb to distinguish the different fragmentation
                self.prefrg=1
                self.userfrg=self.userfrg+data
                self.userlengthfrg=self.userlengthfrg+length
                self.previousserverseqnb=serversequencenb
            if (frg==0 and self.prefrg==1 and serversequencenb > self.previousserverseqnb):
                self.userfrg=self.userfrg+data
                self.prefrg=0
                self.userlist=self.userfrg
                self.userlistlength=self.userlengthfrg+length
                self.previousserverseqnb=serversequencenb
                self.flaguser=1                                                      ###flag indicate that client has already received the userlist

            if(frg==0 and self.prefrg==0):
                self.userlist=data
                self.userlistlength=length
                self.flaguser=1
            if(self.location==0 or self.location==1):
                self.flagmovie=1    
        if(self.flaguser==1 and self.flagmovie==1):      ## to check that client has received both movielist and userlist
            i=0
            j=0
            self.userListstock=[]
            movieListresult=[]
           
            while(i<self.userlistlength):                
                            
                namelength=struct.unpack_from('>B',self.userlist[i])[0]
              
                userid=struct.unpack_from('>B',self.userlist[i+1])[0]
              
                status=struct.unpack_from('>B',self.userlist[i+2])[0]
               
                
                
                p=i
                
                
                i=i+3+namelength
                
                usernameoflist=struct.unpack_from(str(i-p-3)+'s',self.userlist[p+3:i])[0]#self.userlist[p+3:i-1]
                userid=struct.unpack_from('>B',self.userlist[p+1])[0]
                if(status==128):
                    tuple1=(userid,usernameoflist,ROOM_IDS.MAIN_ROOM)
                if(status==0):
                    
                    tuple1=(userid,usernameoflist,ROOM_IDS.MOVIE_ROOM)
                    if(destid==self.roomId):                             # for broadcast in a specific movieroom
                        
                        for tuple in self.movieListstock:                
                            if tuple[3]==destid:                         #just get one movie
                                tuple1=(userid,usernameoflist,tuple[0])  # get user list with the movietitle with client in the movingroom
                   
            
                self.userListresult.append((tuple1[1],tuple1[2]))
                self.userListstock.append(tuple1)
            if(self.location==0 or self.location==1):
                
                self.clientProxy.setUserListONE(self.userListresult)
            
            if(self.location==-1):                                        #### location=-1 for the first time client receive movie list after login sucessful
                while(j<self.movielistlength):                
                    namelength=struct.unpack_from('>B',self.movielist[j])[0]
                    roomid=struct.unpack_from('>B',self.movielist[j+1])[0]
                    p=j
                    j=j+2+namelength
                    
                    movietitleoflist=struct.unpack_from(str(j-p-2)+'s',self.movielist[p+2:j])[0]
                  
                    tuplesafe=(movietitleoflist,1,1,roomid)
                    tuple=(movietitleoflist,1,1)
                  
                    self.movieListstock.append(tuplesafe)
                    self.movieListresult.append(tuple)
                ###############use for  and display movie and user list
                self.clientProxy.initCompleteONE(self.userListresult, self.movieListresult)
            self.userListresult=[]
            if roomtype==0:
                self.location=0                                                #successully login and receive userlist and movielist
      
            self.flaguser=0
            self.flagmovie=0
        
        # ACK leave movie room 
        if (ack==1 and mestype==8 and self.location==1):
            self.flag=1
            self.nbtime=0
            #self.timeout(data)
         
            
            self.obj.cancel()     
            self.clientProxy.joinRoomOKONE()
            self.location=0
            self.flag=0
        
        
        else:
            if(ack==1 and mestype==8 and self.location==0):            ## change room
                self.nbtime=0
                self.flag=1
            
               
                self.obj.cancel()
                self.clientsequencenb=self.clientsequencenb+1
                self.location=1
                ipAdresslist= [str(ip1),str(ip2),str(ip3),str(ip4)]
                point="."
                ipAdress = point.join(ipAdresslist)
                
            
                self.clientProxy.userUpdateReceivedONE(self.userName, self.roomName)
            
                self.clientProxy.updateMovieAddressPort(self.roomName,ipAdress,port)
           
                self.clientProxy.joinRoomOKONE()
               
            
                self.flag=0
        ####ACK leave system
        if(ack==1 and mestype==15):
            
            self.flag=1
            self.nbtime=0
            #self.timeout(data)
          
            
            self.obj.cancel()     
            self.clientProxy.leaveSystemOKONE()
            self.location=-1
            self.flag=0
           
        
        
        
        
        
            
            
            
    def getbit(self,byte,start,end):
        return byte/pow(2,8-end)-byte/pow(2,9-start)*pow(2,end-start+1)
    
    pass
