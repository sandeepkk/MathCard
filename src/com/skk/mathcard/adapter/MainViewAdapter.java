package com.skk.mathcard.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skk.mathcard.R;
import com.skk.mathcard.misc.ListOperation;

public class MainViewAdapter extends ArrayAdapter<String>{
	private Context context;
	
	private LayoutInflater mLayoutInflater;
	private List<ListOperation> mOperationData;
	
	public MainViewAdapter(Context context, int resource, List objects) {
		
		
		super(context, resource, objects);
		this.context = context;
		this.mLayoutInflater = LayoutInflater.from(this.context);
		mOperationData=objects;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListOperation lo ;
		
		if (convertView == null) {
		
			convertView = this.mLayoutInflater.inflate(R.layout.operations_row, null);
		}
		lo = (ListOperation) mOperationData.get(position);
		TextView textViewOper = (TextView) convertView.findViewById(R.id.operation);
		textViewOper.setText(lo.getAction());
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageOperation);
		iv.setImageResource(lo.getIcon());
		return convertView;
	}

}
