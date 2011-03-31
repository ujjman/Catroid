/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team 
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.tugraz.ist.catroid.test.filesystem;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.test.AndroidTestCase;
import at.tugraz.ist.catroid.utils.filesystem.FileSystem;

public class FileSystemTest extends AndroidTestCase {

	final String TEST_STRING = new String("Hello Scratch");
	final String TEST_FILENAME = new String("samplefile.spf");

	private FileSystem mFileSystem;
	private Context mCtx;

	protected void setUp() throws Exception {
		super.setUp();

		mFileSystem = new FileSystem();
		try {
			mCtx = getContext().createPackageContext("at.tugraz.ist.catroid", Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			assertFalse(true);
		}

		try {
			// ##### Write a file to the disk #####
			/*
			 * We have to use the openFileOutput()-method the ActivityContext
			 * provides, to protect your file from others and This is done for
			 * security-reasons. We chose MODE_WORLD_READABLE, because we have
			 * nothing to hide in our file
			 */
			FileOutputStream fOut = new FileOutputStream("/sdcard/" + TEST_FILENAME);
			DataOutputStream dos = new DataOutputStream(fOut);
			// Write the string to the file
			dos.writeChars(TEST_STRING);
			/*
			 * ensure that everything is really written out and close
			 */
			dos.flush();
			dos.close();

		} catch (Exception ex) {
			assertFalse(true);
		}

	}

	protected void tearDown() throws Exception {
		File testFile = new File("/sdcard/" + TEST_FILENAME);
		if(testFile != null && testFile.exists()) {
			testFile.delete();
		}
		super.tearDown();
	}

	public void testDeleteFile() {

		boolean deleted = true;
		mFileSystem.deleteFile("/sdcard/" + TEST_FILENAME, mCtx);

		File sdFile = new File("/sdcard/");
		String[] sdFileList = sdFile.list();

		for (int i = 0; i < sdFileList.length; i++) {
			if (sdFileList[i].equals(TEST_FILENAME))
				deleted = false;
		}

		assertTrue(deleted);

	}

	public void testCreateOrOpenFile() {
		mCtx.deleteFile(TEST_FILENAME);

		mFileSystem.createOrOpenFileOutput("/sdcard/" + TEST_FILENAME, mCtx);

		File sdFile = new File("/sdcard/");
		String[] sdFileList = sdFile.list();
		boolean available = false;

		for (int i = 0; i < sdFileList.length; i++) {
			if (sdFileList[i].equals(TEST_FILENAME))
				available = true;
		}

		assertTrue(available);
	}

	public void testSdCardReady() {
		String state = Environment.getExternalStorageState();
		assertTrue(Environment.MEDIA_MOUNTED.equals(state));
	}
}
