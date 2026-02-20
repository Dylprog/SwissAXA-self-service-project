package com.example.axaproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.PolicyViewHolder> {

    public interface OnPolicyClickListener {
        void onPolicyClick(Policy policy);
    }

    public interface OnPolicyCompareListener {
        void onPolicyCompare(Policy policy);
    }

    private final List<Policy> policies;
    private final OnPolicyClickListener listener;
    private final OnPolicyCompareListener compareListener;

    public PolicyAdapter(List<Policy> policies,
                         OnPolicyClickListener listener,
                         OnPolicyCompareListener compareListener) {
        this.policies = policies;
        this.listener = listener;
        this.compareListener = compareListener;
    }

    @NonNull
    @Override
    public PolicyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_policy, parent, false);
        return new PolicyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyViewHolder holder, int position) {
        Policy policy = policies.get(position);
        holder.title.setText(policy.getTitle());
        holder.number.setText("Policy #: " + policy.getNumber());
        holder.expiry.setText("Expires: " + policy.getExpiryDate());
        holder.itemView.setOnClickListener(v -> listener.onPolicyClick(policy));

        if (policy.isExternal()) {
            holder.compareButton.setVisibility(View.VISIBLE);
            holder.compareButton.setOnClickListener(v -> compareListener.onPolicyCompare(policy));
        } else {
            holder.compareButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return policies.size();
    }

    static class PolicyViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView number;
        final TextView expiry;
        final Button compareButton;

        PolicyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.policy_title);
            number = itemView.findViewById(R.id.policy_number);
            expiry = itemView.findViewById(R.id.policy_expiry);
            compareButton = itemView.findViewById(R.id.btn_compare_products);
        }
    }
}
