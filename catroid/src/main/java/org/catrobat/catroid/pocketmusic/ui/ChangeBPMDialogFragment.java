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

package org.catrobat.catroid.pocketmusic.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import org.catrobat.catroid.R;
import org.catrobat.catroid.pocketmusic.note.Project;

public class ChangeBPMDialogFragment extends DialogFragment {

	public static final String TAG = ChangeBPMDialogFragment.class.getSimpleName();

	private NumberPicker numberPicker;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.dialog_pocketmusic_change_bpm, null);

		numberPicker = view.findViewById(R.id.input_bpm);
		numberPicker.setMinValue(5);
		numberPicker.setMaxValue(300);
		numberPicker.setValue(Project.DEFAULT_BEATS_PER_MINUTE);
		//numberPicker.setHint(getActivity().getString(R.string.pocket_music_bpm));

		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.pocket_music_change_bpm)
				.setView(view)
				.setPositiveButton(R.string.ok, null)
				.setNegativeButton(R.string.cancel, null)
				.create();

		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
				buttonPositive.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO
						dismiss();
					}
				});
				buttonPositive.setEnabled(true);
			}
		});

		return alertDialog;
	}
}
