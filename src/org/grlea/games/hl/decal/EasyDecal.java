package org.grlea.games.hl.decal;

import org.grlea.games.hl.decal.batch.EasyDecalBatchProcessor;
import org.grlea.games.hl.decal.gui.Frame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

// $Id: EasyDecal.java,v 1.3 2005-12-25 22:10:05 grlea Exp $
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
 * @version $Revision: 1.3 $
 */
public class
EasyDecal
{
   private static final String VERSION_FILE = "VERSION.txt";

   public static final String PROJECT_NAME = "easy-decal";

   private static final String VERSION_FILE_URL = "/meta-inf/" + PROJECT_NAME + "/" + VERSION_FILE;

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
         System.out.println("<unable to locate version information>");
         System.out.println();
      }

      // Read arguments
      boolean argumentsProvided = argv.length != 0;
      if (argumentsProvided)
         EasyDecalBatchProcessor.startEasyDecalBatch(argv);
      else
         Frame.startEasyDecalGui();
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
         else
         {
            return false;
         }
      }
      catch (IOException e)
      {
         return false;
      }
   }
}