import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.projedeneme.MyDataBase
import com.ahmetefeozenc.projedeneme.R
import com.ahmetefeozenc.projedeneme.UserInfo

class UserAdapter(
    private var userList: List<String>,
    private val context: MyDataBase,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userNameTextView)
        val userTc: TextView = itemView.findViewById(R.id.usertcTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tc = userList[position]

        val userName = context.KullaniciBilgiGetir("kullanicilar", tc, "ad")+" "+context.KullaniciBilgiGetir("kullanicilar", tc, "soyad")
        val userTc = context.KullaniciBilgiGetir("kullanicilar", tc, "tc")

        holder.userName.text = userName
        holder.userTc.text = userTc

        holder.editButton.setOnClickListener {
            val tc = context.KullaniciBilgiGetir("kullanicilar", tc, "tc")

            val userInfoFragment = UserInfo()
            val bundle = Bundle()
            bundle.putString("tc", tc)

            userInfoFragment.arguments = bundle
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, userInfoFragment)
            fragmentTransaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateData(newData: List<String>) {
        userList = newData
        notifyDataSetChanged()
    }

    fun clearData() {
        userList = emptyList()
        notifyDataSetChanged()
    }
}
