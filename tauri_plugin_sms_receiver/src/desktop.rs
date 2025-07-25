use serde::de::DeserializeOwned;
use tauri::{plugin::PluginApi, AppHandle, Runtime};

use crate::models::*;

pub fn init<R: Runtime, C: DeserializeOwned>(
  app: &AppHandle<R>,
  _api: PluginApi<R, C>,
) -> crate::Result<SmsReceiver<R>> {
  Ok(SmsReceiver(app.clone()))
}

/// Access to the smsreceiver APIs.
pub struct SmsReceiver<R: Runtime>(AppHandle<R>);

impl<R: Runtime> SmsReceiver<R> {
  pub fn ping(&self, payload: PingRequest) -> crate::Result<PingResponse> {
    Ok(PingResponse {
      value: payload.value,
    })
  }
}
