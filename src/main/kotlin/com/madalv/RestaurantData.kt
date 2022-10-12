import com.madalv.Food
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RestaurantData(
    @SerialName("restaurant_id") val restaurantID: Int,
    val name: String,
    val address: String,
    @SerialName("menu_items") val menuItems: Int,
    val menu: List<Food>,
    val rating: Double
) {
    override fun toString(): String {
        return "{ID $restaurantID \n name $name \n addr $address \n menu nrs $menuItems \n rating $rating \n $menu}"
    }
}