// Prevents additional console window on Windows in release, DO NOT REMOVE!!
#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]

fn main() {
    sms_alert_app_lib::run()
    .plugin(tauri_plugin_sms_receiver::init())
    .run(tauri::generate_context!())
    .expect("error while running tauri app");
}
