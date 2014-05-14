package com.priyanka.nfcemulation;

import com.priyanka.nfc.utils.NFCConstants;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScanCardActivity extends BaseActivity {

	private TextView resultTextView;
	private Button scanButton;
	private Button mBtnSend;
	private String resultStr;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scancard_layout);
		
		findViews();
	}
	
	private void findViews(){
		resultTextView = (TextView) findViewById(R.id.resultTextView);
		scanButton = (Button) findViewById(R.id.scanButton);
		mBtnSend=(Button)findViewById(R.id.btnsend);
		
		mBtnSend.setVisibility(Button.VISIBLE);			

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (CardIOActivity.canReadCardWithCamera(this)) {
			scanButton.setText("Scan a credit card ");
		} else {
			scanButton.setText("Device Not Suppoted");
		}
	}

	public void onScanPress(View v) {
		Intent scanIntent = new Intent(this, CardIOActivity.class);
		scanIntent.putExtra(CardIOActivity.EXTRA_APP_TOKEN,
				NFCConstants.MY_CARDIO_APP_TOKEN);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
		startActivityForResult(scanIntent, NFCConstants.MY_SCAN_REQUEST_CODE);
	}
	
	public void sendCardInfo(View v){
		
		Intent nfcIntent = new Intent(this, TagViewer.class);
		nfcIntent.putExtra(NFCConstants.CARD_INFO,resultStr);
		startActivity(nfcIntent);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		resultStr="";
		if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
			CreditCard scanResult = data
					.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
			resultStr = "Card Number: " + scanResult.getRedactedCardNumber()
					+ "\n";
			if (scanResult.isExpiryValid()) {
				resultStr += "Expiration Date: " + scanResult.expiryMonth + "/"
						+ scanResult.expiryYear + "\n";
			}

			if (scanResult.cvv != null) {
				// Never log or display a CVV
				resultStr += "CVV has " + scanResult.cvv.length()
						+ " digits.\n";
			}

			if (scanResult.postalCode != null) {
				resultStr += "Postal Code: " + scanResult.postalCode + "\n";
			}
			
			mBtnSend.setVisibility(Button.VISIBLE);			
		} else {
			resultStr = "Scan was canceled.";
			mBtnSend.setVisibility(Button.GONE);
			
		}
		resultTextView.setText(resultStr);

	}

	

}
