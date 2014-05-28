package com.mapzen.route;

import com.mapzen.R;
import com.mapzen.osrm.Instruction;
import com.mapzen.util.DisplayHelper;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends PagerAdapter {
    private List<Instruction> instructions = new ArrayList<Instruction>();
    private Context context;
    private Instruction currentInstruction;

    public RouteAdapter(Context context, List<Instruction> instructions) {
        this.context = context;
        this.instructions = instructions;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        currentInstruction = instructions.get(position);
        View view = View.inflate(context, R.layout.instruction, null);

        if (position == instructions.size() - 1) {
            view.setBackgroundColor(context.getResources().getColor(R.color.destination_green));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.dark_gray));
        }

        TextView fullInstruction = (TextView) view.findViewById(R.id.full_instruction);
        fullInstruction.setText(
                getFullInstructionWithBoldName(currentInstruction.getFullInstruction()));

        TextView fullInstructionAfterAction =
                (TextView) view.findViewById(R.id.full_instruction_after_action);
        fullInstructionAfterAction.setText(
                getFullInstructionWithBoldName(
                        currentInstruction.getFullInstructionAfterAction()));

        ImageView turnIcon = (ImageView) view.findViewById(R.id.turn_icon);
        turnIcon.setImageResource(DisplayHelper.getRouteDrawable(context,
                currentInstruction.getTurnInstruction(), DisplayHelper.IconStyle.WHITE));

        ImageView turnIconAfterAction =
                (ImageView) view.findViewById(R.id.turn_icon_after_action);
        turnIconAfterAction.setImageResource(DisplayHelper.getRouteDrawable(context,
                10, DisplayHelper.IconStyle.WHITE));

        view.setTag("Instruction_" + String.valueOf(position));
        container.addView(view);
        return view;
    }

    private SpannableStringBuilder getFullInstructionWithBoldName(String fullInstruction) {
        final String name = currentInstruction.getName();
        final int startOfName = fullInstruction.indexOf(name);
        final int endOfName = startOfName + name.length();
        final StyleSpan boldStyleSpan = new StyleSpan(Typeface.BOLD);

        final SpannableStringBuilder ssb = new SpannableStringBuilder(fullInstruction);
        ssb.setSpan(boldStyleSpan, startOfName, endOfName, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return ssb;
    }

    @Override
    public int getCount() {
        return instructions.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
