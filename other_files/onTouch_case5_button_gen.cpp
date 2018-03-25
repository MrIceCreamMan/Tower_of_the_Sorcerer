#include <iostream>
using namespace std;

int main(){
	for (int i = 0; i < 24; i ++) {
		cout << "Button btn" << i+1 << " = staff_of_echo_view.findViewById(R.id.button";
		cout << i+1 << ");\n" << "btn" << i+1 << ".setOnClickListener(new View.OnClickListener() {\n";
		cout << "\t@Override\npublic void onClick(View view) {\n";
		cout << "\tif (echo_history[" << i << "] == 0) {\n\t";
		cout << "echo_builder.setMessage(R.string.no_echo);\n";
		cout << "staff_echo_dialog.dismiss();\n} else {\n\t";
		cout << "echo_builder.setMessage(R.string.saint_4);\n";
		cout << "staff_echo_dialog.dismiss();\n}\n";
		cout << "AlertDialog echo_dialog = echo_builder.create();\n";
		cout << "echo_dialog.show();\n}\n});\n";
	}
}