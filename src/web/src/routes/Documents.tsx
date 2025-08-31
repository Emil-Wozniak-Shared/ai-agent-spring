import React from "react";
import { useAppSelector, useAppDispatch } from "../store/hooks";
import {
  searchDocuments,
  createManyDocuments,
  clearSearchResults,
} from "../store/slices/documentSlice";
import { addNotification } from "../store/slices/appSlice";

const Documents: React.FC = () => {
  const dispatch = useAppDispatch();
  const { documents, searchResults, loading, error } = useAppSelector(
    (state) => state.documents,
  );
  const [searchQuery, setSearchQuery] = React.useState("");
  const [newDocs, setNewDocs] = React.useState([{ title: "", content: "" }]);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchQuery.trim()) return;

    try {
      await dispatch(searchDocuments({ query: searchQuery })).unwrap();
      dispatch(
        addNotification({
          message: "Search completed successfully!",
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "Search failed",
          type: "error",
        }),
      );
    }
  };

  const handleCreateDocuments = async (e: React.FormEvent) => {
    e.preventDefault();
    const validDocs = newDocs.filter((doc) => doc.title && doc.content);
    if (validDocs.length === 0) return;

    try {
      await dispatch(createManyDocuments(validDocs)).unwrap();
      setNewDocs([{ title: "", content: "" }]);
      dispatch(
        addNotification({
          message: `${validDocs.length} document(s) created successfully!`,
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "Failed to create documents",
          type: "error",
        }),
      );
    }
  };

  const addDocumentField = () => {
    setNewDocs([...newDocs, { title: "", content: "" }]);
  };

  const updateDocument = (
    index: number,
    field: "title" | "content",
    value: string,
  ) => {
    const updated = [...newDocs];
    updated[index][field] = value;
    setNewDocs(updated);
  };

  return (
    <div className="page">
      <h1>Documents</h1>

      <div className="documents-section">
        <h2>Search Documents</h2>
        <form onSubmit={handleSearch} className="search-form">
          <input
            type="text"
            placeholder="Search query..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            required
          />
          <button type="submit" disabled={loading}>
            {loading ? "Searching..." : "Search"}
          </button>
          <button
            type="button"
            onClick={() => dispatch(clearSearchResults())}
            disabled={searchResults.length === 0}
          >
            Clear Results
          </button>
        </form>

        {searchResults.length > 0 && (
          <div className="search-results">
            <h3>Search Results ({searchResults.length})</h3>
            {searchResults.map((doc) => (
              <div key={doc.id} className="document-card">
                <h4>{doc.title}</h4>
                <p>{doc.content.substring(0, 200)}...</p>
                <small>
                  Created: {new Date(doc.createdAt).toLocaleDateString()}
                </small>
              </div>
            ))}
          </div>
        )}
      </div>

      <div className="documents-section">
        <h2>Create Documents</h2>
        <form onSubmit={handleCreateDocuments}>
          {newDocs.map((doc, index) => (
            <div key={index} className="document-input">
              <input
                type="text"
                placeholder="Document title"
                value={doc.title}
                onChange={(e) => updateDocument(index, "title", e.target.value)}
              />
              <textarea
                placeholder="Document content"
                value={doc.content}
                onChange={(e) =>
                  updateDocument(index, "content", e.target.value)
                }
                rows={3}
              />
            </div>
          ))}
          <div className="form-actions">
            <button type="button" onClick={addDocumentField}>
              Add Another Document
            </button>
            <button type="submit" disabled={loading}>
              {loading ? "Creating..." : "Create Documents"}
            </button>
          </div>
        </form>
      </div>

      {error && <div className="error-message">{error}</div>}
    </div>
  );
};

export default Documents;
