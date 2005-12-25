package org.grlea.games.hl.decal.gui;

// $Id: GuiResources.java,v 1.1 2005-12-25 22:10:09 grlea Exp $
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
 * <p>A helper class for obtaining resources associated with the decal gui.</p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
GuiResources
extends ResourcesBase
{
   private static final String RESOURCE_NAME = "EasyDecal_Gui";

   private static GuiResources singleton;

   private
   GuiResources()
   throws MissingResourceException
   {
      super(RESOURCE_NAME);
   }

   public static synchronized GuiResources
   instance()
   {
      if (singleton == null)
         singleton = new GuiResources();
      return singleton;
   }

   public String
   frameTitle()
   {
      return getString("frameTitle");
   }

   public String
   failedToInitialiseSwing()
   {
      return getString("failedToInitialiseSwing");
   }

   public String
   failedToInitialiseGuiCommands()
   {
      return getString("failedToInitialiseGuiCommands");
   }

   public String
   donateMessage()
   {
      return getString("donateMessage");
   }

   public String
   progressBarString(int completed, int total)
   {
      return format2("progressBarString", new Integer(completed), new Integer(total));
   }

   public String
   failedToOpenBrowser()
   {
      return getString("failedToOpenBrowser");
   }

   public String
   errorsDialogTitle()
   {
      return getString("errorsDialogTitle");
   }

   public String
   errorsDialogHeader()
   {
      return getString("errorsDialogHeader");
   }

   public String
   errorsDialogCloseButton()
   {
      return getString("errorsDialogCloseButton");
   }

   public String
   fatalErrorDialogTitle()
   {
      return getString("fatalErrorDialogTitle");
   }
}