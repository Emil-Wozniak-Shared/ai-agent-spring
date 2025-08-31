import React from "react";
import type { User } from "~/store/slices/userSlice";

interface UserCardProps {
  user: User;
  isEditing: boolean;
  onEdit: () => void;
  onSave: (userData: User) => void;
  onCancel: () => void;
}

const UserCard: React.FC<UserCardProps> = ({
  user,
  isEditing,
  onEdit,
  onSave,
  onCancel,
}) => {
  const [editData, setEditData] = React.useState({
    ...user,
    name: user.name,
    email: user.email,
  });

  const handleSave = () => {
    onSave(editData);
  };

  if (isEditing) {
    return (
      <div className="user-card editing">
        <input
          value={editData.name}
          onChange={(e) => setEditData({ ...editData, name: e.target.value })}
        />
        <input
          value={editData.email}
          onChange={(e) => setEditData({ ...editData, email: e.target.value })}
        />
        <div className="card-actions">
          <button onClick={handleSave}>Save</button>
          <button onClick={onCancel}>Cancel</button>
        </div>
      </div>
    );
  }

  return (
    <div className="user-card">
      <h3>{user.name}</h3>
      <p>{user.email}</p>
      <button onClick={onEdit}>Edit</button>
    </div>
  );
};

export default UserCard;
