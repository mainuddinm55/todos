package info.learncoding.todo.data.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import info.learncoding.todo.BR
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Todo(
    var id: String? = null,
    var title: String? = null,
    @ServerTimestamp
    var date: Date? = null
) : BaseObservable(), Parcelable {

    @IgnoredOnParcel
    @get:Bindable
    @get:Exclude
    @set:Exclude
    var selected = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }

}
