package th.co.itg.ktb_demo.Customer;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

import th.co.itg.ktb_demo.R;

/**
 * Created by ToCHe on 3/9/2018 AD.
 */
public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder>  {

    ArrayList<Customer> customerList = new ArrayList<>();
    CustomerListener listener;

    public CustomerAdapter(CustomerListener listener) {
        this.listener = listener;
    }

    public void addCustomer(Customer cus){
        customerList.add(cus);
        notifyDataSetChanged();
    }

    public void updateCustomer(ArrayList<Customer> data){
        customerList.clear();
        customerList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer,parent,false);
        return new CustomerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, int position) {
        holder.bind(customerList.get(position));
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    class CustomerHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView txtName;

        public CustomerHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.rootCard);
            txtName = itemView.findViewById(R.id.txtName);
        }


        public void bind(Customer data){
            txtName.setText(data.name);
            PushDownAniamtion(cardView,data);
        }
        private void PushDownAniamtion(View view,Customer data){
            PushDownAnim.setOnTouchPushDownAnim(view)
                    .setOnClickListener(view1 -> {
                        switch (view.getId()) {
                            case R.id.rootCard:
                                listener.onClickCustomer(data);
                                break;
                        }
                    });
        }
    }

    interface CustomerListener{
        void onClickCustomer(Customer data);
    }
}
