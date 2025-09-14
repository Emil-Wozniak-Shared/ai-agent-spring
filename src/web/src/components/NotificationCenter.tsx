import { useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import { useAppDispatch, useAppSelector } from "~/store/hooks";

export default function NotificationCenter() {
  const dispatch = useAppDispatch();
  const { notifications } = useAppSelector((state) => state.app);
  const notify = (msg: string) => toast(msg);

  useEffect(() => {
    notifications.forEach((notification) => {
      toast(notification.message, { type: notification.type });
    });
  }, [notifications]);
  return <ToastContainer />;
}
