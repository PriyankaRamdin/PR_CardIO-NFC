package com.priyanka.nfcemulation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.priyanka.nfc.record.ParsedNdefRecord;
import com.priyanka.nfc.record.SmartPoster;
import com.priyanka.nfc.record.TextRecord;
import com.priyanka.nfc.record.UriRecord;

public class NdefMessageParser {

    private NdefMessageParser() {

    }

    public static List<ParsedNdefRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) {
        List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();
        for (final NdefRecord record : records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record));
            } else {
            	elements.add(new ParsedNdefRecord() {
					@Override
					public View getView(Activity activity, LayoutInflater inflater, ViewGroup parent, int offset) {
				        TextView text = (TextView) inflater.inflate(R.layout.tag_text, parent, false);
				        text.setText(new String(record.getPayload()));
				        return text;
					}
            		
            	});
            }
        }
        return elements;
    }
}
