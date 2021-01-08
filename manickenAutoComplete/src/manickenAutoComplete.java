/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
  Part of the Processing project - http://processing.org

  Copyright (c) 2008 Ben Fry and Casey Reas
  Copyright (c) 2020 Jannik LS Svensson (1984)- Sweden

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software Foundation,
  Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.manicken;

import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.Box;
import javax.swing.JOptionPane; // used by settings dialog

import java.util.ArrayList;

import processing.app.Editor;
import processing.app.PreferencesData;
import processing.app.EditorToolbar;
import processing.app.EditorTab;
import processing.app.syntax.SketchTextArea;
import processing.app.tools.Tool;

import com.manicken.AutoCompleteProvider;
import com.manicken.CustomMenu;
import com.manicken.SettingsDialog;
import com.manicken.Reflect2;

/**
 * 
 */
public class manickenAutoComplete implements Tool
{
	int retryCount = 0;

	Editor editor;// for the plugin
	EditorToolbar originalEditorToolBar; // used to restore the original toolbar when this plugin is deactivated
	
	String thisToolMenuTitle = "Manicken AutoComplete";

	public void init(Editor editor) { // required by tool loader
		this.editor = editor;
		// workaround to make sure that init is run after the Arduino IDE gui has loaded
		// otherwise any System.out(will never be shown at the init phase) 
		editor.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowOpened(java.awt.event.WindowEvent e) {
			  init();
			}
		});
	}

	public void run() {// required by tool loader
		// this is not used when using custom menu (see down @initMenu())
	}

	public String getMenuTitle() {// required by tool loader
		return thisToolMenuTitle;
	}

	private void Activate()
	{
		PreferencesData.setBoolean("manicken.autoComplete.activated", true);
		ActivateAutoCompleteFunctionality();
		
		System.err.println("AutoComplete - Activated");
	}

	private void Deactivate()
	{
		PreferencesData.setBoolean("manicken.autoComplete.activated", false);
		
		System.err.println("AutoComplete - Deactivated (at next restart)");
	}

	/**
	 * This is the code that runs after the Arduino IDE GUI has been loaded
	 */
	private void init() {
		try{
			CustomMenu cm = new CustomMenu(this.editor, thisToolMenuTitle, 
				new JMenuItem[] {
					CustomMenu.Item("Activate", event -> Activate()),
					CustomMenu.Seperator(),
					CustomMenu.Item("Deactivate", event -> Deactivate()),
				});
			cm.Init(true);
			
			if (PreferencesData.getBoolean("manicken.autoComplete.activated", false))
			{
				Activate();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(thisToolMenuTitle + " could not start!!!");
			return;
		}
	}

	public String GetJarFileDir() {
		try {
			File file = new File(manickenAutoComplete.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			return file.getParent();
		}catch (Exception e) { e.printStackTrace(); return ""; }
	}

	public void ActivateAutoCompleteFunctionality() {
		ArrayList<EditorTab> tabs = (ArrayList<EditorTab>) Reflect2.GetField("tabs", this.editor);
		for (int i = 0; i < tabs.size(); i++) {
			SketchTextArea textArea = tabs.get(i).getTextArea();
			AutoCompleteProvider acp = new AutoCompleteProvider(textArea, GetJarFileDir());
		}
	}

	private void ShowSettingsDialog()
	{
		SettingsDialog sd = new SettingsDialog();
		//sd.txtRetryCount.setText(Integer.toString(retryCount));
		int result = JOptionPane.showConfirmDialog(editor, sd, "Upload without compile - settings" ,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
		if (result != JOptionPane.OK_OPTION) {
			System.out.println("Cancelled");
			return;
		}
		int rc = 0;
		try {
			//rc = Integer.parseInt(sd.txtRetryCount.getText());
			//retryCount = rc;
			//PreferencesData.setInteger("manicken.uploadOnly.retryCount", retryCount);
		}
		catch (Exception e) {System.err.println("Warning Retry count cannot be empty!\nprevious value used:" + retryCount);}
	
	}

}
