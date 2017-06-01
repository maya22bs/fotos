package com.fotos.fotos.cardHandling;

import android.app.AlertDialog;
import android.content.Context;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fotos.fotos.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image;
        TextView name;
        TextView question;
        TextView option1;
        TextView option2;
        TextView sponsored;
        ImageView sponsorLogo;
        TextView shareFoto;
        TextView likeFoto;
        TextView commentFoto;
        boolean left_is_correct = false;
        boolean is_sponsored = false;

        CardViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            image = (ImageView)itemView.findViewById(R.id.card_image);
            name = (TextView)itemView.findViewById(R.id.card_name);
            question  = (TextView)itemView.findViewById(R.id.card_question);
            option1  = (TextView)itemView.findViewById(R.id.card_option1);
            option2  = (TextView)itemView.findViewById(R.id.card_option2);
            sponsored = (TextView)itemView.findViewById(R.id.card_sponsored);
            sponsorLogo  = (ImageView)itemView.findViewById(R.id.card_sponsorLogo);
            shareFoto = (TextView)itemView.findViewById(R.id.card_share);
            likeFoto = (TextView)itemView.findViewById(R.id.card_like);
            commentFoto = (TextView)itemView.findViewById(R.id.card_comment);
        }
    }

    List<Card> cards;

    public CardViewAdapter(List<Card> cards){
        this.cards = cards;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        cardViewHolder.left_is_correct = cards.get(i).left_is_correct;
        cardViewHolder.is_sponsored = cards.get(i).sponsored;
        // set image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap imageBitmap = (cards.get(i).imageBitmap == null) ? BitmapFactory.decodeResource(cardViewHolder.cardView.getResources(),
                cards.get(i).imageDrawable, options) : cards.get(i).imageBitmap;
        cardViewHolder.image.setImageBitmap(imageBitmap);
        cardViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set Preview Dialog
                final Dialog nagDialog = new Dialog(view.getContext(), android.R.style.Theme_Translucent_NoTitleBar);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.preview_image);
                Button btnClose = (Button) nagDialog.findViewById(R.id.btnIvClose);
                ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.iv_preview_image);
                // rotate image
                if (view.getResources().getConfiguration().orientation ==  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getWidth(), imageBitmap.getHeight(), true);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    // set rotated image as preview
                    ivPreview.setBackgroundDrawable(new BitmapDrawable(rotatedBitmap));
                } else {
                    ivPreview.setBackgroundDrawable(new BitmapDrawable(imageBitmap));
                }

                View.OnClickListener closeListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        nagDialog.dismiss();
                    }
                };
                btnClose.setOnClickListener(closeListener);
                ivPreview.setOnClickListener(closeListener);
                nagDialog.show();
            }
        });

        // set name
        cardViewHolder.name.setText("By " + cards.get(i).name);
        Typeface type = Typeface.createFromAsset(cardViewHolder.cardView.getContext().getAssets(), "fonts/Roboto-Thin.ttf");
        cardViewHolder.name.setTypeface(type);
        // set question
        cardViewHolder.question.setText(cards.get(i).question);
        // set option1
        cardViewHolder.option1.setText(cards.get(i).option1);
        cardViewHolder.option1.setVisibility(!cards.get(i).sponsored ? View.VISIBLE : View.INVISIBLE);
        ((ImageView)cardViewHolder.cardView.findViewById(R.id.card_arrrow_left)).setVisibility(!cards.get(i).sponsored ? View.VISIBLE : View.INVISIBLE);
        // set option2
        cardViewHolder.option2.setText(cards.get(i).option2);
        // set sponsored
        cardViewHolder.sponsored.setVisibility(cards.get(i).sponsored ? View.VISIBLE : View.INVISIBLE);
        // set sponsor logo
        cardViewHolder.sponsorLogo.setVisibility(View.INVISIBLE);
        if (cardViewHolder.is_sponsored) {
            BitmapFactory.Options sponsorOptions = new BitmapFactory.Options();
            options.inScaled = false;
            Bitmap sponsorImageBitmap = null;
            if (cards.get(i).sponsorLogo.equals("mcdonalds")){
                sponsorImageBitmap = BitmapFactory.decodeResource(cardViewHolder.cardView.getResources(),
                        R.drawable.logo_mcdonalds, sponsorOptions);
            } else if (cards.get(i).sponsorLogo.equals("starbucks")){
                sponsorImageBitmap = BitmapFactory.decodeResource(cardViewHolder.cardView.getResources(),
                        R.drawable.logo_starbucks, options);
            } else if (cards.get(i).sponsorLogo.equals("ticketmaster")){
                sponsorImageBitmap = BitmapFactory.decodeResource(cardViewHolder.cardView.getResources(),
                        R.drawable.logo_ticketmaster, options);
            }

            if (sponsorImageBitmap != null) {
                cardViewHolder.sponsorLogo.setImageBitmap(sponsorImageBitmap);
                cardViewHolder.sponsorLogo.setVisibility(View.VISIBLE);
            }
        }
        // Like/Comment/Share
        cardViewHolder.shareFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(v.getContext(), "foto shared!",
                        Toast.LENGTH_SHORT);
                TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                tv.setBackgroundColor(0);
                toast.show();
            }
        });

        cardViewHolder.likeFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(v.getContext(), "foto liked!",
                        Toast.LENGTH_SHORT);
                TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                tv.setBackgroundColor(0);
                toast.show();
            }
        });

        cardViewHolder.commentFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

                final EditText edittext= new EditText(v.getContext());
                edittext.setTextColor(Color.WHITE);
                alert.setTitle("Comment on foto");

                alert.setView(edittext);

                alert.setPositiveButton("Comment", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String inputText = edittext.getText().toString();
                        //For now...
                        Toast.makeText(alert.getContext(), String.format("Comment:\n %s \n Sent", inputText),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("Never mind", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void remove(int position) {
        cards.remove(position);
        notifyItemRemoved(position);
    }

    // Adds card to the bottom.
    public void add_card(Card card) {
        cards.add(card);
        notifyItemInserted(cards.size() - 1);
    }

    // Handles swiping answer.
    public void handleAnswer(int direction, Context context, RecyclerView.ViewHolder viewHolder) {
        CardViewHolder cardViewHolder = (CardViewAdapter.CardViewHolder)viewHolder;
        boolean answer = (direction == ItemTouchHelper.LEFT) ? true : false; // boolean for if answered left
        if (cardViewHolder.is_sponsored == true) {
            Toast.makeText(context, "Coming soon...",
                    Toast.LENGTH_SHORT).show();
        } else if (answer == cardViewHolder.left_is_correct) {
            Toast.makeText(context, "Correct! +1",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Incorrect, maybe next time",
                    Toast.LENGTH_SHORT).show();
        }


    }
}
