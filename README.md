# LobbySystem

This here is the **LobbySystem** for our Minigame Network!

## Supported Languages

The plugin currently supports two languages:

- **German**: `resources/lobby_de.properties`
- **English (US)**: `resources/lobby_en_US.properties`

To add a new language, create a new file named e.g `lobby_en_US.properties`.

## Permissions

### Commands

| Command        | Permission                        | Usage                          |
|----------------|-----------------------------------|--------------------------------|
| `/lobbysystem` | `lobbysystem.command.lobbysystem` | `use`, `set`, `teleport`, `tp` |
| `/build`       | `lobbysystem.command.build`       |                                |
| `/build`       | `lobbysystem.command.build.other` | `[Player Name]`                |
| `/fish`        |                                   |                                |

### Lobby Switcher

| Permission                           | Usage                               |
|--------------------------------------|-------------------------------------|
| `lobbysystem.server.silentlobby`     | Allowed to join the SilentLobby     |
| `lobbysystem.server.premiumlobby`    | Allowed to join the PremiumLobby    |
| `lobbysystem.server.buildserver`     | Allowed to join the BuildServer     |
| `lobbysystem.server.developerserver` | Allowed to join the DeveloperServer |

### Items

| Permission                       | Usage                       |
|----------------------------------|-----------------------------|
| `lobbysystem.item.flightfeather` | Use the feather item to fly |
| `lobbysystem.item.rankboots`     | Set boots for specific rank |

## How To (Compiling Jar From Source)
To compile this LobbySystem, you need JDK 27 and an internet connection.

Clone this repo, run `mvn clean install` from your terminal. You can find the compiled jar in the `target/` directory.

## Contributing

Please let us know if you encounter any issues.
If so, just follow the steps below.

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes.
4. Submit a PR with a clear description of your changes.

## Contact

For any other questions or support, feel free to join our [Discord server](https://go.soulsmc.eu/discord) and contact us.

Thanks for contributing~
