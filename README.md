# Happy Robot Death - A tiny Bomberman homage

This game was conceived during a course in Java at KTH. It is a homage to some of my favourite games growing up, such as Zelda and Bomberman. The game originally had network support (implemented via Kryonet) for up to four players. It was a very simple, straightforward implementation with all physics calculations being done on the server side, and rendering and input polling on the client side. Due to haphazard implementation (the game was done in about a week, including reading up on libgdx and Kryonet), the network support has been stripped with the intention of implementing a better solution (read: actual protocol) later on. Due to limited peripheral support, only two players are supported locally.

## Controls:

General: 
*Q* -- Quits the application
*R* -- Rerolls the box placement.

Player 1:

*W,A,S,D* 	Movement


*T* 		Bomb

Player 2:

*Arrows*	Movement


*P*			Bomb


## Known issues:

Full screen rendering is broken on Retina displays (issue with LWJGL).
This can be remedied by opening HDRWindowed.jar and simply maximizing 
the window on Macbook Pros

For further reading:
https://github.com/libgdx/libgdx/issues/2932