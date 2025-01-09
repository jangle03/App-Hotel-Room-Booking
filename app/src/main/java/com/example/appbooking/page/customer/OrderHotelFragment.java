package com.example.appbooking.page.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appbooking.R;

public class OrderHotelFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_hotel, container, false);

        ImageView hotelImage = view.findViewById(R.id.hotelImage);
        TextView hotelName = view.findViewById(R.id.hotelName);
        TextView hotelLocation = view.findViewById(R.id.hotelLocation);
        TextView hotelDescription = view.findViewById(R.id.hotelDescription);
        TextView hotelServices = view.findViewById(R.id.hotelServices);
        LinearLayout highlightedPlaces = view.findViewById(R.id.highlightedPlaces);

        hotelImage.setImageResource(R.drawable.anhkhachsan);
        hotelName.setText("Khách Sạn GRFOUR");
        hotelLocation.setText(" 123 Đường XYZ, TP. Hồ Chí Minh");
        hotelDescription.setText(" Khách sạn ABC là một điểm đến hoàn hảo cho những ai muốn tận hưởng không gian sang trọng và tiện nghi hiện đại. Tọa lạc tại trung tâm thành phố, khách sạn mang đến sự kết hợp tuyệt vời giữa thiết kế kiến trúc độc đáo, dịch vụ chuẩn quốc tế và không gian yên tĩnh, lý tưởng để nghỉ dưỡng. Với đội ngũ nhân viên tận tâm và chuyên nghiệp, khách sạn ABC cam kết mang lại những trải nghiệm khó quên cho du khách. Ngoài ra, bạn còn có cơ hội thưởng thức những món ăn đặc sắc tại nhà hàng cao cấp, thư giãn tại spa hoặc tận hưởng bầu không khí thư thái bên hồ bơi vô cực. Đây chính là nơi lý tưởng để bạn tận hưởng những khoảnh khắc đáng nhớ bên gia đình, bạn bè hay đối tác.");
        hotelServices.setText("- Hồ bơi\n- Nhà hàng cao cấp\n- Spa thư giãn\n- Phòng gym hiện đại");

        String[] places = {"Chợ Bến Thành", "Phố đi bộ Nguyễn Huệ", "Dinh Độc Lập", "Bảo tàng Thành phố", "Công viên 23/9", "Nhà thờ Đức Bà", "Bưu điện Trung tâm Sài Gòn", "Bảo tàng Chứng tích Chiến tranh", "Tòa nhà Bitexco", "Cầu Ánh Sao", "Phố Tây Bùi Viện", "Công viên Tao Đàn", "Nhà hát Thành phố"};
        for (String place : places) {
            TextView placeText = new TextView(getContext());
            placeText.setText("- " + place);
            placeText.setTextSize(16);
            placeText.setTextColor(getResources().getColor(android.R.color.black));
            placeText.setPadding(8, 4, 8, 4);
            highlightedPlaces.addView(placeText);
        }

        return view;
    }
}
