package org.grlea.games.hl.decal;

// $Id: EasyDecalBatchProcessor.java,v 1.1 2004-11-25 05:07:18 grlea Exp $
// Copyright (c) 2004 Graham Lea. All rights reserved.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import org.grlea.games.hl.wad.Wad;
import org.grlea.games.hl.wad.WadEntry;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
EasyDecalBatchProcessor
{
   public static void
   main(String[] argv)
   {
      System.out.println();

      // Read arguments
      // TODO (grahaml) Read more arguments
      if (argv.length == 0)
         usage();

      DecalSizes sizes = new DecalSizes();

      boolean errors = false;
      for (int i = 0; i < argv.length; i++)
      {
         String imageFilename = argv[i];
         try
         {
            File imageFile = new File(imageFilename);
            System.out.println(imageFile);
            System.out.println("   Reading");

            if (!imageFile.exists())
            {
               error(imageFile + " does not exist.");
               errors = true;
               continue;
            }

            if (imageFile.isDirectory())
            {
               error(imageFile + " is a directory.");
               errors = true;
               continue;
            }

            if (!imageFile.canRead())
            {
               error(imageFile + " cannot be read.");
               errors = true;
               continue;
            }

            // Read image in
            BufferedImage image = null;
            try
            {
               image = ImageIO.read(imageFile);

               if (image == null)
               {
                  error("Your Java installation is not capable of reading this type of image.");
                  errors = true;
                  continue;
               }
            }
            catch (IOException e)
            {
               error("Failure while reading image file: " + e);
               errors = true;
               continue;
            }

            // Find best size
            System.out.print("   Converting ");
            DecalSize size = sizes.getClosestDimension(image);
            System.out.println("(" + size.getWidth() + "x" + size.getHeight() + ")");

            WadEntry entry = new WadEntry("pldecal", size.getWidth(), size.getHeight());
            entry.createAllMips(image);

            // Create palette
            entry.generatePaletteFromAllMips();

            // Write to WAD
            System.out.println("   Writing WAD");
            Wad wad = new Wad();
            wad.addEntry(entry);

            File directory = imageFile.getParentFile();
            File wadFile = new File(directory, imageFile.getName() + ".wad");
            try
            {
               wad.write(wadFile);
            }
            catch (IOException e)
            {
               error("Failure while writing WAD file: " + e);
               errors = true;
               continue;
            }

            // Test code to write a PNG file of MIP1
            // TODO (grahaml) Make this a command line option? Or cfg file?
            boolean writeMipsToPngs = false;

            if (writeMipsToPngs)
            {
               try
               {
                  System.out.println("   Writing MIPs to " + imageFile.getName() + ".mip*.png");
                  String mip1Filename = imageFile.getName() + ".mip1.png";
                  String mip2Filename = imageFile.getName() + ".mip2.png";
                  String mip3Filename = imageFile.getName() + ".mip3.png";
                  String mip4Filename = imageFile.getName() + ".mip4.png";
                  boolean imagesWritten;
                  imagesWritten = ImageIO.write(entry.getMip1Converted(), "png", new File(directory, mip1Filename));
                  imagesWritten &= ImageIO.write(entry.getMip2Converted(), "png", new File(directory, mip2Filename));
                  imagesWritten &= ImageIO.write(entry.getMip3Converted(), "png", new File(directory, mip3Filename));
                  imagesWritten &= ImageIO.write(entry.getMip4Converted(), "png", new File(directory, mip4Filename));
                  if (!imagesWritten)
                     throw new IOException("Failed to find PNG writer.");

//                  BufferedImage mip1 = entry.getMip1();
//                  BufferedImage mip1Bmp = new BufferedImage(mip1.getWidth(), mip1.getHeight(), BufferedImage.TYPE_INT_RGB);
//                  Graphics2D graphics = mip1Bmp.createGraphics();
//                  graphics.drawImage(mip1, 0, 0, null);
//                  graphics.dispose();
//                  ImageIO.write(mip1Bmp, "bmp", new File(directory, "MIP1.bmp"));
               }
               catch (IOException e)
               {
                  error("Failure while writing PNG file(s): " + e);
                  errors = true;
               }
            }

//             Test code to show MIP1 in a frame
            boolean shopMip1InFrame = false;
            if (shopMip1InFrame)
            {
               System.out.println("   Displaying MIP");
               ImageIcon icon = new ImageIcon(entry.getMip1Converted());
               JFrame frame = new JFrame(imageFilename);
               frame.getContentPane().add(new JButton(icon));
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.pack();
               frame.setVisible(true);
            }
         }
         catch (RuntimeException e)
         {
            error("Uncaught runtime error: " + e);
            e.printStackTrace();
         }
         catch (Error e)
         {
            error("Uncaught runtime error: " + e);
            e.printStackTrace();
         }

         System.out.println("   Done");
         System.out.println();
      }

      if (errors)
      {
         System.err.println();
         System.err.println("*** WARNING *** ");
         System.err.println();
         System.err.println("Errors occurred during batch processing.");
         System.err.println("See output for details.");
         System.err.println();
      }
   }

   private static void
   error(String error)
   {
      System.err.println();
      System.err.println("   ERROR: " + error);
      System.err.println();
   }

   private static void
   usage()
   {
      System.err.println("usage: <jvm> " + EasyDecalBatchProcessor.class.getName() + " <image_file> [<image_file> ...]");
      System.exit(1);
   }

}