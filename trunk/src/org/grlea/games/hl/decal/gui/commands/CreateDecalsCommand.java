package org.grlea.games.hl.decal.gui.commands;

// $Id: CreateDecalsCommand.java,v 1.1 2005-12-25 22:10:12 grlea Exp $
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

import org.grlea.games.hl.decal.gui.Model;
import org.grlea.games.hl.decal.gui.SourceFile;
import org.grlea.games.hl.decal.gui.SourceFileList;
import org.grlea.log.SimpleLogger;

import foxtrot.Job;
import foxtrot.Worker;
import org.pietschy.command.ActionCommand;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
CreateDecalsCommand
extends ActionCommand
implements SourceFileList.Listener
{
   private static final SimpleLogger log = new SimpleLogger(CreateDecalsCommand.class);

   private Model model;

   public
   CreateDecalsCommand(Model model)
   {
      super("CreateDecals");

      this.model = model;

      model.getFileList().addListener(this);
      updateEnabledStatus();
   }

   protected void
   handleExecute()
   {
      log.entry("handleExecute()");

      setEnabled(false);

      Worker.post(new Job()
      {
         public Object
         run()
         {
            log.entry("run()");
            log.debug("Creating Decals");
            model.getProcessor().createDecals(model);
            log.exit("run()");
            return null;
         }
      });

      log.exit("handleExecute()");
   }

   public void
   fileAdded(SourceFile file)
   {
      updateEnabledStatus();
   }

   public void
   fileRemoved(SourceFile file)
   {
      updateEnabledStatus();
   }

   private void
   updateEnabledStatus()
   {
      setEnabled(model.getFileList().getSize() != 0);
   }
}