# TexturePatcher Alpha
A Spigot plugin for allowing new textures to be created. 
Please note that this is in alpha at the moment, and is unfunctional.

### How it Works
The plugin, on the server initializing, gets called by another plugin with a block type and an image. This image is saved in a custom texture pack with a damage value as the way to figure out what kind of block it is. Then the plugin uploads this to a HTTP server or Dropbox, to provide a player-downloadable link. When a player joins, the texturepack is set to the one on Dropbox. The plugin can call another function to get an `ItemStack` that has the correct damage value to make the texture show up. The blocks or items will look like a new item or block, but will have a custom name and texture. 

### Setup
Don't. This is in alpha, and is fairly unstable. IF you want to compile it yourself, that might work, but it won't do much of anything.
