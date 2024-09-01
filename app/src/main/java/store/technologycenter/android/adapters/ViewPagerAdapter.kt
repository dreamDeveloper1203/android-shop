package store.technologycenter.android.adapters


import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import store.technologycenter.android.R
import store.technologycenter.android.listeners.KtItemClickListener
import store.technologycenter.android.models.SlideModel


class ViewPagerAdapter(
        context: Context?,
        imageList: List<SlideModel>,
        private var radius: Int,
        private var errorImage: Int,
        private var placeholder: Int,
        private var centerCrop: Boolean?,
        private var displayMetrics: DisplayMetrics
) : PagerAdapter() {

    private var imageList: List<SlideModel>? = imageList
    private var layoutInflater: LayoutInflater? =
            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
    private var mContext: Context? = context;
    private var itemClickListener: KtItemClickListener? = null

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return imageList!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {

        val itemView = layoutInflater!!.inflate(R.layout.pager_row, container, false)

        val imageView = itemView.findViewById<ImageView>(R.id.image_view)
        //val progressBar = itemView.findViewById<ProgressBar>(R.id.image_view_progress_bar)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.linear_layout)
        val textView = itemView.findViewById<TextView>(R.id.text_view)
        val screenWidth = displayMetrics.widthPixels;
        if (imageList!![position].title != null) {
            textView.text = imageList!![position].title
        } else {
            linearLayout.visibility = View.INVISIBLE
        }

        if (imageList!![position].imageUrl == null) {
            Glide.with(mContext!!)
                    .load(imageList!![position].imagePath!!)
                    .override(screenWidth, 0)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(errorImage)
                    .into(imageView);
        } else {
            Glide.with(mContext!!)
                    .load(imageList!![position].imageUrl!!)
                    .override(screenWidth, 0)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(errorImage)
                    .into(imageView);
        }

        container.addView(itemView)

        imageView.setOnClickListener { itemClickListener?.onItemSelected(position) }

        return itemView
    }

    private fun requestListener(progressBar: ProgressBar): RequestListener<Drawable?> {
        return object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
            ): Boolean {
                progressBar.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
            ): Boolean {
                progressBar.visibility = View.GONE
                return false
            }
        }
    }

    fun setItemClickListener(itemClickListener: KtItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}