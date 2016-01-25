package pl.elpassion.door.githubclient

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class UserSearchResponse(val items: List<User>)
class User(@SerializedName(value="login") override val name: String, val avatarUrl: String, val reposUrl: String) : GithubSearchItem, Parcelable {

    companion object{
        final val CREATOR : Parcelable.Creator<User> = object : Parcelable.Creator<User> {

            override fun createFromParcel(parcel : Parcel) : User{
                return User(parcel)
            }

            override fun newArray(size : Int) : Array<User?> {
                return arrayOfNulls<User>(size)
            }
        }
    }

    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString(), parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags : Int) {
        parcel.writeString(name)
        parcel.writeString(avatarUrl)
        parcel.writeString(reposUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

}
