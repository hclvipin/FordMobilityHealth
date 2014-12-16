package com.openxc.ford.mHealth.demo.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.SmsManager;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.openxc.ford.mHealth.demo.AppLog;
import com.openxc.ford.mHealth.demo.R;
import com.openxc.ford.mHealth.demo.model.Patient;

public class PatientsListAdapter extends BaseAdapter implements OnClickListener {
	private final String TAG = AppLog.getClassName();

	private final String BTN_MSG_OK = "OK";
	private final String BTN_MSG_CANCEL = "Cancel";
	private final String MSG_TITLE = "Enter your message";
	private final String MSG_SMS_TEXT = "We are close to you, be available.";

	private final String MSG_PATIENT = "Patient Name : ";
	private final String MSG_ADDRESS = "Patient Address : ";
	private final String MSG_CONTACT = "Contact Number : ";

	private Context mContext = null;
	private ArrayList<Patient> mPatientsList = null;
	public LayoutInflater mInflater = null;

	public static class ViewHolderClass {
		TextView txVwPatientName;
		TextView txVwPatientAddress;
		TextView txVwPatientContact;
		Button btnNotifyPatient;
	}

	public PatientsListAdapter(Context context, ArrayList<Patient> patientsList) {
		this.mContext = context;
		this.mPatientsList = patientsList;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return mPatientsList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppLog.enter(TAG, AppLog.getMethodName());

		ViewHolderClass holder;
		if (convertView == null) {
			holder = new ViewHolderClass();
			convertView = mInflater.inflate(R.layout.patients_list_item, null);

			holder.txVwPatientName = (TextView) convertView
					.findViewById(R.id.txtView_patient_name);
			holder.txVwPatientAddress = (TextView) convertView
					.findViewById(R.id.txtView_patient_address);
			holder.txVwPatientContact = (TextView) convertView
					.findViewById(R.id.txtView_patient_contactNo);
			holder.btnNotifyPatient = (Button) convertView
					.findViewById(R.id.btn_notification);
			holder.btnNotifyPatient.setTag(position);
			holder.btnNotifyPatient
					.setOnClickListener(PatientsListAdapter.this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderClass) convertView.getTag();
		}

		Patient patient = mPatientsList.get(position);

		holder.txVwPatientName.setText(MSG_PATIENT + patient.getName());
		holder.txVwPatientAddress.setText(MSG_ADDRESS + patient.getAddress());
		holder.txVwPatientContact.setText(MSG_CONTACT
				+ patient.getContactNumber());

		AppLog.exit(TAG, AppLog.getMethodName());
		return convertView;
	}

	@Override
	public void onClick(View view) {
		AppLog.enter(TAG, AppLog.getMethodName());

		int position = (Integer) view.getTag();
		final String contactNo = mPatientsList.get(position).getContactNumber();

		AppLog.info(TAG, MSG_CONTACT + contactNo);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		View promptsView = inflater.inflate(R.layout.search_patient_dialog,
				null);
		TextView txVwTitle = (TextView) promptsView
				.findViewById(R.id.tx_search_criteria);
		final EditText edTxInput = (EditText) promptsView
				.findViewById(R.id.ed_SearchValue);

		edTxInput.setInputType(InputType.TYPE_CLASS_TEXT);

		InputFilter[] inputFilter = new InputFilter[1];
		inputFilter[0] = new InputFilter.LengthFilter(160);
		edTxInput.setFilters(inputFilter);

		edTxInput.setText(MSG_SMS_TEXT);

		txVwTitle.setText(MSG_TITLE);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mContext);
		alertDialogBuilder.setView(promptsView);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton(BTN_MSG_OK,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								String text = edTxInput.getText().toString();
								SmsManager smsManager = SmsManager.getDefault();
								smsManager.sendTextMessage(contactNo, null,
										text, null, null);
							}
						})
				.setNegativeButton(BTN_MSG_CANCEL,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

		AppLog.exit(TAG, AppLog.getMethodName());
	}
}
