/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.devices.mindstorms.ev3;

import android.content.Context;
import android.test.AndroidTestCase;

import org.catrobat.catroid.common.bluetooth.ConnectionDataLogger;
import org.catrobat.catroid.devices.mindstorms.ev3.LegoEV3;
import org.catrobat.catroid.devices.mindstorms.ev3.LegoEV3Impl;

public class LegoEV3ImplTest extends AndroidTestCase {

	private Context applicationContext;

	private LegoEV3 ev3;
	ConnectionDataLogger logger;

	private static final int BASIC_MESSAGE_BYTE_OFFSET = 6;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		applicationContext = this.getContext().getApplicationContext();

		ev3 = new LegoEV3Impl(this.applicationContext);
		logger = ConnectionDataLogger.createLocalConnectionLogger();
		ev3.setConnection(logger.getConnectionProxy());
	}

	public void testSimplePlayToneTest() {

		int inputHz = 9000;
		int expectedHz = 9000;
		int durationInMs = 3000;
		int volume = 100;

		ev3.initialise();
		ev3.playTone(inputHz, durationInMs, volume);

		byte[] setOutputState = logger.getNextSentMessage(0, 2);

		int offset = BASIC_MESSAGE_BYTE_OFFSET + 3; // 1 byte command, 1 bytes volume, 1 byte datatype

		assertEquals((byte) expectedHz, setOutputState[offset]);
		assertEquals((byte) (expectedHz >> 8), setOutputState[offset + 1]);
	}

	public void testPlayToneHzOverMaxValue() {

		// MaxHz = 10000;
		int inputHz = 16000;
		int expectedHz = 10000;
		int durationInMs = 5000;
		int volume = 100;

		ev3.initialise();
		ev3.playTone(inputHz, durationInMs, volume);

		byte[] setOutputState = logger.getNextSentMessage(0, 2);

		int offset = BASIC_MESSAGE_BYTE_OFFSET + 3; // 1 byte command, 1 bytes volume, 1 byte datatype

		assertEquals((byte) expectedHz, setOutputState[offset]);
		assertEquals((byte) (expectedHz >> 8), setOutputState[offset + 1]);
	}

	public void testPlayToneCheckDuration() {

		int inputHz = 9000;
		int durationInMs = 2000;
		int volume = 100;
		int expectedDurationInMs = 2000;

		ev3.initialise();
		ev3.playTone(inputHz, durationInMs, volume);

		byte[] setOutputState = logger.getNextSentMessage(0, 2);

		int offset = BASIC_MESSAGE_BYTE_OFFSET + 6; // 1 byte command, 1 bytes volume, 3 bytes freq, 1 byte datatype

		assertEquals((byte) expectedDurationInMs, setOutputState[offset]);
		assertEquals((byte) (expectedDurationInMs >> 8), setOutputState[offset + 1]);
	}

	public void testPlayToneCheckVolume() {

		int inputHz = 9000;
		int durationInMs = 2000;
		int volume1 = 100;
		int expectedVolumeLevel1 = 13;

		ev3.initialise();
		ev3.playTone(inputHz, durationInMs, volume1);

		byte[] setOutputState = logger.getNextSentMessage(0, 2);

		int offset = BASIC_MESSAGE_BYTE_OFFSET + 1; // 1 byte command

		assertEquals((byte) expectedVolumeLevel1, setOutputState[offset]);

		int volume2 = 25;
		int expectedVolumeLevel2 = 4;
		ev3.playTone(inputHz, durationInMs, volume2);

		setOutputState = logger.getNextSentMessage(0, 2);

		assertEquals((byte) expectedVolumeLevel2, setOutputState[offset]);
	}

	public void testPlayToneWithZeroDuration() {

		int inputHz = 13000;
		int inputDurationInMs = 0;
		int volume = 100;

		ev3.initialise();
		ev3.playTone(inputHz, inputDurationInMs, volume);

		byte[] command = logger.getNextSentMessage(0, 2);

		assertEquals(null, command);
	}

	public void testPlayToneWithZeroVolume() {

		int inputHz = 13000;
		int inputDurationInMs = 0;
		int volume = 0;

		ev3.initialise();
		ev3.playTone(inputHz, inputDurationInMs, volume);

		byte[] command = logger.getNextSentMessage(0, 2);

		assertEquals(null, command);
	}

	public void testSimpleLED() {

		int ledStatus = 0x04;
		int expectedLedStatus = 0x04;

		ev3.initialise();
		ev3.setLed(ledStatus);

		byte[] setOutputState = logger.getNextSentMessage(0, 2);

		int offset = BASIC_MESSAGE_BYTE_OFFSET + 2; // 1 byte command, 1 byte datatype

		assertEquals((byte) expectedLedStatus, setOutputState[offset]);
	}
}
