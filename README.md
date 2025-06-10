# Space Invaders
## References
[Space Invader](https://github.com/janbodnar/Java-Space-Invaders)

A classic Space Invaders game implementation in Java using Swing for the graphical interface.

## Description

This is a Java implementation of the classic arcade game Space Invaders. Players control a spaceship at the bottom of the screen and must defend against waves of descending alien invaders. The game features:

- Player-controlled spaceship that moves left and right
- Multiple rows of alien enemies that move across and down the screen
- Shooting mechanics to destroy aliens
- Enemy bombs that can destroy the player
- Score tracking through alien kills
- Game over conditions for both victory and defeat

## Controls

- **Left Arrow**: Move spaceship left
- **Right Arrow**: Move spaceship right
- **Space**: Fire weapon

## Game Rules

- Destroy all aliens (24 in total) to win the game
- Avoid enemy bombs
- Don't let aliens reach the ground, or it's game over
- Player dies if hit by an enemy bomb

## Project Structure

```
src/
├── edu/au/gdd/
│   ├── Global.java         # Game constants and configuration
│   ├── Main.java          # Game entry point
│   ├── SpaceInvaders.java # Main game window setup
│   └── sprite/
│       ├── Enemy.java     # Enemy alien implementation
│       ├── Player.java    # Player spaceship implementation
│       ├── Shot.java      # Projectile implementation
│       └── Sprite.java    # Base sprite class
```

## Technical Details

- Built with Java and Swing
- Uses sprite-based graphics
- Implements game loop with constant refresh rate
- Object-oriented design with inheritance for game entities

## Running the Game

1. Ensure you have Java installed on your system
2. Compile the source files
3. Run the game using:
   ```
   java gdd.Main
   ```

## Development Environment Setup

### Installing Java with SDKMan

1. Install SDKMan
   ```
   curl -s "https://get.sdkman.io" | bash
   source "$HOME/.sdkman/bin/sdkman-init.sh"
   ```

2. Install Java using SDKMan
   ```
   sdk list java              # List available Java versions
   sdk install java 17.0.9-tem  # Install latest LTS version (recommended)
   sdk default java 17.0.9-tem  # Set as default
   ```

3. Verify installation
   ```
   java -version
   javac -version
   ```

### Setting up Visual Studio Code

1. Install Visual Studio Code
   - Download from [VS Code website](https://code.visualstudio.com/)
   - Install the downloaded package

2. Install Required Extensions
   - Open VS Code
   - Go to Extensions view (Cmd+Shift+X on macOS, Ctrl+Shift+X on Windows/Linux)
   - Install the following extensions:
     - Extension Pack for Java (includes):
       - Language Support for Java by Red Hat
       - Debugger for Java
       - Test Runner for Java
       - Maven for Java
       - Project Manager for Java
     - Java Code Generators

3. Configure VS Code Java Settings
   - Open Command Palette (Cmd+Shift+P on macOS, Ctrl+Shift+P on Windows/Linux)
   - Type "Java: Configure Java Runtime"
   - Select the installed JDK from SDKMan

4. Open Project
   - File -> Open Folder
   - Select the space-invaders project folder
   - Wait for VS Code to load and index the Java project

Now you're ready to develop and run the Space Invaders game!

## Requirements

- Java Runtime Environment (JRE)
- Java Development Kit (JDK) for compilation