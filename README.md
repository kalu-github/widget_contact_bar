![image](https://github.com/153437803/ContactDemo/blob/master/image_2018-04-21.gif )

```
适用场景：

<declare-styleable name="ContactBar">
    <attr name="cb_text_color_normal" format="color|reference" />
    <attr name="cb_text_color_select" format="color|reference" />
    <attr name="cb_text_size_normal" format="dimension|reference" />
    <attr name="cb_text_size_pressed" format="dimension|reference" />
    <attr name="cb_text_width" format="dimension|reference" />
    <attr name="cb_text_height" format="dimension|reference" />
    <attr name="cb_text_bg_color_normal" format="color|reference" />
    <attr name="cb_text_bg_color_press" format="color|reference" />
    <attr name="cb_hint_height" format="dimension|reference" />
    <attr name="cb_hint_width" format="dimension|reference" />
    <attr name="cb_hint_text_size" format="dimension|reference" />
    <attr name="cb_circle_color" format="color|reference" />
    <attr name="cb_circle_radius" format="dimension|reference" />
    <attr name="cb_hint_radius" format="dimension|reference" />
</declare-styleable>
```

```

int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
bar.scrollLetter(datas.get(firstVisibleItemPosition).getIndex());

final ContactBar bar = findViewById(R.id.sidebar);
        bar.setOnBarChangeListener(new ContactBar.OnBarChangeListener() {
            @Override
            public void onBarChange(String letter) {

                int position = 0;
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getIndex().equals(letter)) {
                        position = i;
                        break;
                    }
                }

                moveToPosition(linearLayoutManager, position);
            }
        });
```
