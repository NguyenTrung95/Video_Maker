package com.devchie.videomaker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.devchie.videomaker.R;
import com.devchie.videomaker.activities.MainActivity;
import com.devchie.videomaker.model.MySharedPreferences;

public class RateDialog extends Dialog implements View.OnClickListener {
    private final Activity context;
    private boolean exit;
    private ImageView[] imageViewStars = new ImageView[5];
    private ImageView iv_star_1;
    private ImageView iv_star_2;
    private ImageView iv_star_3;
    private ImageView iv_star_4;
    private ImageView iv_star_5;
    private LottieAnimationView lottieAnimationView;
    private ViewGroup ratingBar;
    private int star_number = 0;
    private TextView tvLater;
    private TextView tvSubmit;

    public RateDialog(Activity activity, boolean z) {
        super(activity);
        this.context = activity;
        this.exit = z;
        setContentView(R.layout.dialog_smart_rate);
        initView();
    }

    private void initView() {
        this.tvSubmit = (TextView) findViewById(R.id.tv_submit);
        this.tvLater = (TextView) findViewById(R.id.tv_later);
        LottieAnimationView lottieAnimationView2 = (LottieAnimationView) findViewById(R.id.lottie);
        this.lottieAnimationView = lottieAnimationView2;
        lottieAnimationView2.setProgress(1.0f);
        this.ratingBar = (ViewGroup) findViewById(R.id.scaleRatingBar);
        this.tvSubmit.setOnClickListener(this);
        this.tvLater.setOnClickListener(this);
        this.iv_star_1 = (ImageView) findViewById(R.id.star_1);
        this.iv_star_2 = (ImageView) findViewById(R.id.star_2);
        this.iv_star_3 = (ImageView) findViewById(R.id.star_3);
        this.iv_star_4 = (ImageView) findViewById(R.id.star_4);
        this.iv_star_5 = (ImageView) findViewById(R.id.star_5);
        this.iv_star_1.setOnClickListener(this);
        this.iv_star_2.setOnClickListener(this);
        this.iv_star_3.setOnClickListener(this);
        this.iv_star_4.setOnClickListener(this);
        this.iv_star_5.setOnClickListener(this);
        ImageView[] imageViewArr = this.imageViewStars;
        imageViewArr[0] = this.iv_star_1;
        imageViewArr[1] = this.iv_star_2;
        imageViewArr[2] = this.iv_star_3;
        imageViewArr[3] = this.iv_star_4;
        imageViewArr[4] = this.iv_star_5;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.tv_later) {
            if (id != R.id.tv_submit) {
                switch (id) {
                    case R.id.star_1:
                        this.star_number = 1;
                        setStarBar();
                        return;
                    case R.id.star_2:
                        this.star_number = 2;
                        setStarBar();
                        return;
                    case R.id.star_3:
                        this.star_number = 3;
                        setStarBar();
                        return;
                    case R.id.star_4:
                        this.star_number = 4;
                        setStarBar();
                        return;
                    case R.id.star_5:
                        this.star_number = 5;
                        setStarBar();
                        return;
                    default:
                        return;
                }
            } else {
                int i = this.star_number;
                if (i >= 4) {
                    this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + this.context.getPackageName())));
                    MySharedPreferences.getInstance(this.context).putBoolean("rated", true);
                } else if (i > 0) {


                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  context.getString(R.string.email_feedback)});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Video Maker Feedback");


                    emailIntent.setType("message/rfc822");

                    try {
                        context.startActivity(Intent.createChooser(emailIntent,
                                "Send email using..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context,
                                "No email clients installed.",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    this.ratingBar.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.shake));
                }
            }
        } else if (this.exit) {
            Activity activity = this.context;
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).showGoodBye();
            } else {
                activity.finish();
            }
        } else {
            dismiss();
        }
    }

    private void setStarBar() {
        int i = 0;
        while (true) {
            ImageView[] imageViewArr = this.imageViewStars;
            if (i >= imageViewArr.length) {
                break;
            }
            if (i < this.star_number) {
                imageViewArr[i].setImageResource(R.drawable.ic_star);
            } else {
                imageViewArr[i].setImageResource(R.drawable.ic_star_blur);
            }
            i++;
        }
        if (this.star_number < 4) {
            this.tvSubmit.setText(R.string.rating_dialog_feedback_title);
        } else {
            this.tvSubmit.setText(R.string.rating_dialog_submit);
        }
        this.lottieAnimationView.setProgress(((float) this.star_number) / 5.0f);
    }
}
