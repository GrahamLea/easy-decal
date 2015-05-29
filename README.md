# Easy Decal

Easy Decal is a utility/wizard that I wrote in 2004/2005 that's used to create custom "decals" for Counter-Strike
and other games that run on the original Half-Life engine.

## Why did I build it?

At the time, the only other way to create custom decals was to follow complicated tutorials that detailed how to
* scale and crop an image to the correct size (x and y must both be a multiple of 16, no greater than 256, with a total area no greater than 10,752!),
* convert the colour model to a 256-colour palette,
* then somehow convert the output into a WAD file.

Due to the simplicity of many graphics programs' palette-choosing algorithms, results were often poor,
and because of the complicated instructions there were many, many forum posts about "I can't get this to work."

After creating one or two myself manuall, I thought it seemed like a small but interesting problem to attack 
with code, so I set to work creating first a command line tool for myself to use and, eventually, a GUI wizard
that allowed anyone to use the tool simply.

## Did anyone use it?

Easy Decal was *amazingly* popular - as in, thousands and thousands of downloads over many months.
Despite having a 'Donate' button in the wizard, I only ever received one donation, which turned out to be a mistake.
I consider it one of my biggest entrepreneurial mistakes that I never considered better monetisation strategies
for Easy Decal, and intead gave away for free a popular niche tool to a very big market.

## What's interesting about it?

From a code perspective, there's a couple of interesting things about Easy Decal.

Firstly, I wrote my own implementation of a median-cut octree colour quantisation algorithm that created
fantastic palettes when the images were converted to 256 colours. This wasn't something that I knew anything about, 
so I had the joy of reading a number of academic papers about various methods and then turning some of them
into code.

Secondly, it contains a mostly-complete Java implementation for writing out 
[Half-Life WAD3 files](https://developer.valvesoftware.com/wiki/WAD "WAD file format description on Valve Dev community") (the old-school version of what are called "materials" in Source), complete with auto-generating MIPs for different levels of detail (LOD).
