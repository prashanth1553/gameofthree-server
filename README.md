
This is a Spring Boot application which hosts a websocket server. To make it simple, at a time server is allowing only 2 players. 
When two players are joined game starts. 

Steps to run this project.
1. mvn clean install
2. docker build -t dprashanthrgukt/gameofthreeserver:latest 
3. If you don't want to build the image on your own you can skip above steps and run below command. I have pushed the image to public repository 

   docker run --network="host" dprashanthrgukt/gameofthreeserver:latest
   
Steps to play this game(AUTOMATIC and Manual Mode Supported)
1. Launch Game of three server and launch [Client applicaion](https://github.com/prashanth1553/gameofthree-client/blob/master/README.md) client in atleast two windows
2. When there are two players are joined, the server starts the game. Each player will get a message saying "Game has started.."
3. In your window when you see your oppoent move like below <br/>
  "Opponent move : Added number is -1, Resulting Number is 177." <br/>
  A. You can answer it by following game rules  based on Resulting Number given by opponent move <br/>
  B. You can say autmatic "AUTOMATIC" then the programs responds to the opponent automatically. <br/>

[player_1_moves.png] (https://github.com/prashanth1553/gameofthree-server/blob/master/player_1_moves.png) and [player_2_moves.png](https://github.com/prashanth1553/gameofthree-server/blob/master/player_2_moves.png) screenshot shows how game proceeds.
