# Carolo Cup Simulator

## Creating a map to drive on

The map is loaded from a square RGB png image. Both dimensions of the map have to be powers of 2.
I suggest 4096x4096 to have sufficient resolution. 

This gives us three 8-bit channels to encode information.

**BLUE CHANNEL:** Blue is used to render the white markings on the road. Draw with a blue component from 0 to 255 to display a shade of white/gray on these pixels with 0 being black and 255 white.

**RED CHANNEL:** Every pixel with a larger than 0 red component is considered a penalty area. Driving into this area will incur a penalty equal to the value stored in the red channel. This way you can set penalties from 1 to 255.

 **GREEN CHANNEL:** Not currently used, but will be used to draw and render obstacles and signs. The value can correspond to height and/or type of the entity.
 
Example: If you draw a purple pixel it will be rendered white according to the blue value and it will count as a penalty zone according to the red value.

## Running the simulation

1. Put the map you want to run in `src/main/resources/assets/textures/<mapName>.png`
2. Specify the `<mapName>` you want to use in `src/main/java/sim/Sim.java` 
3. Run `src/main/java/main.java`

There are a few more options like car start position that can be configured in `Sim.java`.
Currently the car can be controlled with WASD. In the future there will be an InputStream to control it. 

Switch the cameras with `Q` or `1`,  `2` and `3`: Bird's Eye (Top Down), Follow (3rd Person), Perspective (1st Person).

## Saving the output to one image per frame
To train or validate a model you can enable or disable saving the screen output via the "start/stop recording" button in the info window.
While enabled, one screenshot per frame is saved to `/src/main/resources/output/` with the filename corresponding to date and time.

## Send motor controls to the car
Not implemented yet. In the future you will be able to send a control vector to the car to steer it, fully automating the validation and training of models.    