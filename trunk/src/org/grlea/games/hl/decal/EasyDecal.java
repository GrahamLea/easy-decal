package org.grlea.games.hl.decal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

// $Id: EasyDecal.java,v 1.2 2004-11-26 12:27:37 grlea Exp $
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

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
EasyDecal
{
   private static final String VERSION_FILE = "VERSION.txt";

   private static final String VERSION_FILE_URL = "/meta-inf/" + VERSION_FILE;

   public static void
   main(String[] argv)
   {
      StringWriter versionText = new StringWriter(256);
      PrintWriter out = new PrintWriter(versionText);
      boolean readVersion = readVersionFromJar(out);
      out.close();

      if (readVersion)
      {
         System.out.println();
         System.out.println(versionText.toString());
         System.out.println();
      }
      else
      {
         System.out.println();
         System.out.println("Easy Decal");
         System.out.println("<unable to detect version>");
         System.out.println();
      }

      // Read arguments
      // TODO (grahaml) Plug the GUI in here:
      if (argv.length == 0)
         usage();

      EasyDecalBatchProcessor batchProcessor = new EasyDecalBatchProcessor(argv);
      DecalCreator creator = new DecalCreator();
      creator.createDecals(batchProcessor, batchProcessor);
   }

   private static boolean
   readVersionFromJar(PrintWriter out)
   {
      try
      {
         URL versionUrl = EasyDecal.class.getResource(VERSION_FILE_URL);
         if (versionUrl != null)
         {
            InputStream urlStream = versionUrl.openStream();
            InputStreamReader reader = new InputStreamReader(urlStream);
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null)
            {
               out.println(line);
            }
            in.close();
            return true;
         }
      }
      catch (IOException e)
      {}
      return false;
   }

   private static void
   usage()
   {
      System.err.println("The current version of Easy Decal requires you to supply images.");
      System.err.println("This can be done either by dragging them onto the starting script or ");
      System.err.println("listing them on a command line.");
      System.exit(-1);
   }
}