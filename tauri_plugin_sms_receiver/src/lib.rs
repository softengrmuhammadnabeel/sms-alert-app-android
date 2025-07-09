use tauri::{
  plugin::{Builder, TauriPlugin},
  Manager, Runtime,
};

pub use models::*;

#[cfg(desktop)]
mod desktop;
#[cfg(mobile)]
mod mobile;

mod commands;
mod error;
mod models;

pub use error::{Error, Result};

#[cfg(desktop)]
use desktop::SmsReceiver;
#[cfg(mobile)]
use mobile::SmsReceiver;

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the smsreceiver APIs.
pub trait SmsReceiverExt<R: Runtime> {
  fn sms_receiver(&self) -> &SmsReceiver<R>;
}

impl<R: Runtime, T: Manager<R>> crate::SmsReceiverExt<R> for T {
  fn sms_receiver(&self) -> &SmsReceiver<R> {
    self.state::<SmsReceiver<R>>().inner()
  }
}

/// Initializes the plugin.
pub fn init<R: Runtime>() -> TauriPlugin<R> {
  Builder::new("smsreceiver")
    .invoke_handler(tauri::generate_handler![commands::ping])
    .setup(|app, api| {
      #[cfg(mobile)]
      let sms_receiver = mobile::init(app, api)?;
      #[cfg(desktop)]
      let sms_receiver = desktop::init(app, api)?;
      app.manage(sms_receiver);
      Ok(())
    })
    .build()
}
