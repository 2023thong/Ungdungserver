package gmail.com.ass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Nhanvien extends BaseAdapter {

    private List<User1> users;
    private LayoutInflater inflater;

    public Nhanvien(Context context, List<User1> users) {
        this.users = users;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.hienthi, parent, false);
        }

        User1 user1 = users.get(position);

        TextView tvMasv = convertView.findViewById(R.id.EdManv);
        TextView tvTensv = convertView.findViewById(R.id.edTennv);
        TextView tvDiem = convertView.findViewById(R.id.edSdt1);
        TextView tvSdt = convertView.findViewById(R.id.edDiachi1);


        tvMasv.setText(user1.getManv());
        tvTensv.setText(user1.getTennv());
        tvDiem.setText(user1.getSdt());
        tvSdt.setText(user1.getDiachi());


        return convertView;
    }
}

