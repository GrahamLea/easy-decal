package org.grlea.games.hl.decal.batch;

// $Id: BatchResources.java,v 1.1 2005-12-25 22:10:06 grlea Exp $
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

import java.util.MissingResourceException;

/**
 * <p>A helper class for obtaining resources associated with the decal batch processor.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
class
BatchResources extends ResourcesBase
{
   private static final String RESOURCE_NAME = "EasyDecal_Batch";

   private static BatchResources singleton;

   private
   BatchResources()
   throws MissingResourceException
   {
      super(RESOURCE_NAME);
   }

   public static synchronized BatchResources
   instance()
   {
      if (singleton == null)
         singleton = new BatchResources();
      return singleton;
   }

   public String
   beginningBatch()
   {
      return getString("beginningBatch");
   }

   public String
   error(String error)
   {
      return format1("error", error);
   }

   public String
   errorsDuringBatch()
   {
      return getString("errorsDuringBatch");
   }

   public String
   seeOutput()
   {
      return getString("seeOutput");
   }

   public String
   uncaughtRuntime()
   {
      return getString("uncaughtRuntime");
   }

   public String
   warning()
   {
      return getString("warning");
   }

   public String
   writingMipsToPngs()
   {
      return getString("writingMipsToPngs");
   }
}