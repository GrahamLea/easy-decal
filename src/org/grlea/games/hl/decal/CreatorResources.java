package org.grlea.games.hl.decal;

// $Id: CreatorResources.java,v 1.1 2005-12-25 22:10:05 grlea Exp $
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

import org.grlea.util.ResourcesBase;

import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;

/**
 * <p>A helper class for obtaining resources associated with the decal batch processor.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
class
CreatorResources
extends ResourcesBase
{
   private static final String RESOURCE_NAME = "EasyDecal_Creator";

   private static CreatorResources singleton;

   private
   CreatorResources()
   throws MissingResourceException
   {
      super(RESOURCE_NAME);
   }

   public static synchronized CreatorResources
   instance()
   {
      if (singleton == null)
         singleton = new CreatorResources();
      return singleton;
   }

   public String
   fileDoesntExist(File imageFile)
   {
      return format1("fileDoesntExist", imageFile);
   }

   public String
   fileIsDirectory(File imageFile)
   {
      return format1("fileIsDirectory", imageFile);
   }

   public String
   fileCannotBeRead(File imageFile)
   {
      return format1("fileCannotBeRead", imageFile);
   }

   public String
   javaCannotReadImage()
   {
      return getString("javaCannotReadImage");
   }

   public String
   failureWhileReadingImage(IOException e)
   {
      return format1("failureWhileReadingImage", e);
   }

   public String
   failureWhileWritingWadFile(IOException e)
   {
      return format1("failureWhileWritingWadFile", e);
   }

   public String
   failureWhileWritingPngFiles(IOException e)
   {
      return format1("failureWhileWritingPngFiles", e);
   }
}