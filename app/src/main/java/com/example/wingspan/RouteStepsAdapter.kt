import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapthree.RouteInstructionsAdapter
import com.example.mapthree.RouteStep
import com.example.wingspan.R

class RouteStepsAdapter(private val routeSteps: List<RouteStep>) : RecyclerView.Adapter<RouteStepsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stepInstruction: TextView = itemView.findViewById(R.id.stepInstruction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_direction, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentStep = routeSteps[position]
        holder.stepInstruction.text = currentStep.instruction
    }

    override fun getItemCount() = routeSteps.size
}