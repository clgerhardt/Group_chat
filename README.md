# Build Guide

`javac Server.java`
`javac Client.java`

On one terminal run:
`java Server`
Then in other new terminals run:
`java Client`

To PM a another user, you only need to type in this format.
Otherwise you'll be prompted to attempt your message again. 
`@ username message`

## Client

Global Variables:
- String: hostname
- int: port
- String: username
- Thread: OutputThread
- Thread: InputThread

Methods:
- Client()
- start_client()
- set_uesrname(String username)
- get_username()
- main(String [] args)

## Output Thread sub class of Client class

Global Variables:
- BufferedReader: reader
- Socket: socket
- Client: client

Methods:
- OutputThread(Socket socket, Client client)
- run()

## Input Thread sub class of Client class

Global Variables:
- PrintWriter: writer
- Socket: socket
- Client: client

Methods:
- InputThread(Socket socket, Client client)
- run()

## Server

Global Variables:
- int: port
- Set<String>: usernames
- Set<UserThreads>: user_threads
- HashMap<String, UserThread> user_map

Methods:
- Server()
- run_server()
- send_to_group(String message, UserThread dont_send_to_this_user)
- sent_to_person(String message, String specific_user, UserThread user)
- add_username(String username, UserThread user_thread)
- remove_user(String user_name, UserThread user)
- get_username()
- main(String [] args)

## User Thread sub class of Server class

Global Variables:
- Socket: socket
- Server: server
- PrintWriter: writer

Methods:
- UserThread(Socket socket, Server server)
- send_message(String message)
- run()


