import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.projedeneme.Transaction
import com.ahmetefeozenc.projedeneme.databinding.ItemTransactionHistoryBinding

class TransactionAdapter(private val transactions: MutableList<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(private val binding: ItemTransactionHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.userNameTextView.text = transaction.userName
            binding.miktarTextView.text = if (transaction.amount < 0) {
                "-${-transaction.amount} TL"
            } else {
                "+${transaction.amount} TL"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ItemTransactionHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun addTransaction(transaction: Transaction) {
        transactions.add(transaction)
        notifyDataSetChanged()
    }
}
