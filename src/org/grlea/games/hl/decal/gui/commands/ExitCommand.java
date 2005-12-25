package org.grlea.games.hl.decal.gui.commands;

// $Id: ExitCommand.java,v 1.1 2005-12-25 22:10:12 grlea Exp $
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

import org.grlea.log.SimpleLogger;

import org.pietschy.command.ActionCommand;

import java.awt.Window;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
ExitCommand
extends ActionCommand
{
   private static final SimpleLogger log = new SimpleLogger(ExitCommand.class);
   
   public
   ExitCommand()
   {
      super("Exit");

      // TODO (grahaml) Set state depending on whether processing is occurring.
   }

   protected void
   handleExecute()
   {
      log.entry("handleExecute()");

      Window invokerWindow = getInvokerWindow();
      if (invokerWindow != null)
         invokerWindow.dispose();

      System.exit(0);

      log.exit("handleExecute()");
   }
}