import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.projedeneme.DolarTransaction
import com.ahmetefeozenc.projedeneme.Transaction
import com.ahmetefeozenc.projedeneme.databinding.ItemDollarTransactionBinding

class DollarTransactionAdapter(private val transactions: MutableList<DolarTransaction>) :
    RecyclerView.Adapter<DollarTransactionAdapter.DollarTransactionViewHolder>() {

    inner class DollarTransactionViewHolder(private val binding: ItemDollarTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: DolarTransaction) {
            if (transaction.dolaramount.toString() == "1.0") {
                binding.islemTextView.text = "${transaction.tlamount} Dolar Alım"
                binding.tldovizmiktarTextView.text = "-${transaction.islem} TL" // Alım için eksi işareti eklendi
            } else if (transaction.dolaramount.toString() == "0.0") {
                binding.islemTextView.text = "${transaction.tlamount} Dolar Satım"
                binding.tldovizmiktarTextView.text = "+${transaction.islem} TL" // Satış için artı işareti eklendi
            } else {
                binding.islemTextView.text = transaction.islem
                binding.tldovizmiktarTextView.text = transaction.dolaramount.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DollarTransactionViewHolder {
        val binding = ItemDollarTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DollarTransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DollarTransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
    fun addTransaction(transaction: DolarTransaction) {
        transactions.add(transaction)
        notifyDataSetChanged()
    }
}
