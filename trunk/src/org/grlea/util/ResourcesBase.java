package org.grlea.util;

// $Id: ResourcesBase.java,v 1.1 2005-12-25 22:10:14 grlea Exp $
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

import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class 
ResourcesBase
{
   private static final SimpleLogger log = new SimpleLogger(ResourcesBase.class);

   private final ResourceBundle resources;

   public
   ResourcesBase(String resourceName)
   {
      log.dbo(DebugLevel.L5_DEBUG, "Locale", Locale.getDefault());
      resources = ResourceBundle.getBundle(resourceName);
      log.dbo(DebugLevel.L6_VERBOSE, "resources", resources);
   }

   protected String
   getString(String key)
   {
      return resources.getString(key);
   }

   protected String
   format1(String key, Object argument1)
   {
      String formatString = resources.getString(key);
      MessageFormat format = new MessageFormat(formatString);
      return format.format(new Object[] {argument1});
   }

   protected String
   format2(String key, Object argument1, Object argument2)
   {
      String formatString = resources.getString(key);
      MessageFormat format = new MessageFormat(formatString);
      return format.format(new Object[] {argument1, argument2});
   }

   public final ResourceBundle
   getResourceBundle()
   {
      return resources;
   }
}