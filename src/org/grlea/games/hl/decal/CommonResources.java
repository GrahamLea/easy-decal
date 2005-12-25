package org.grlea.games.hl.decal;

// $Id: CommonResources.java,v 1.1 2005-12-25 22:10:05 grlea Exp $
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

import java.io.IOException;
import java.util.MissingResourceException;

/**
 * <p>A helper class for obtaining resources associated with the decal batch processor.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
CommonResources
extends ResourcesBase
{
   private static final String RESOURCE_NAME = "EasyDecal_Common";

   private static CommonResources singleton;

   private
   CommonResources()
   throws MissingResourceException
   {
      super(RESOURCE_NAME);
   }

   public static synchronized CommonResources
   instance()
   {
      if (singleton == null)
         singleton = new CommonResources();
      return singleton;
   }

   public String
   converting()
   {
      return getString("converting");
   }

   public String
   done()
   {
      return getString("done");
   }

   public String
   errorReadingConfigFile(IOException e)
   {
      return format1("errorReadingConfigFile", e);
   }

   public String
   reading()
   {
      return getString("reading");
   }

   public String
   writingWad()
   {
      return getString("writingWad");
   }
}