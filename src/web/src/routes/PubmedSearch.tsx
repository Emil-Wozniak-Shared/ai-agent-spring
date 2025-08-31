import React from "react";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import {
  searchPubmedArticles,
  searchPubmedArticlesByIds,
  fetchArticleAbstract,
  setCurrentArticle,
} from "~/store/slices/pubmedSlice";
import { addNotification } from "~/store/slices/appSlice";

const PubmedSearch: React.FC = () => {
  const dispatch = useAppDispatch();
  const { articles, currentArticle, loading, error } = useAppSelector(
    (state) => state.pubmed,
  );
  const [searchQuery, setSearchQuery] = React.useState("");
  const [searchIds, setSearchIds] = React.useState("");

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchQuery.trim()) return;

    try {
      await dispatch(
        searchPubmedArticles({
          query: searchQuery,
          limit: 20,
        }),
      ).unwrap();
      dispatch(
        addNotification({
          message: "PubMed search completed!",
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "PubMed search failed",
          type: "error",
        }),
      );
    }
  };

  const handleSearchByIds = async (e: React.FormEvent) => {
    e.preventDefault();
    const ids = searchIds
      .split(",")
      .map((id) => id.trim())
      .filter((id) => id);
    if (ids.length === 0) return;

    try {
      await dispatch(searchPubmedArticlesByIds(ids)).unwrap();
      dispatch(
        addNotification({
          message: "Articles fetched by IDs!",
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "Failed to fetch articles by IDs",
          type: "error",
        }),
      );
    }
  };

  const handleFetchAbstract = async (articleId: string) => {
    try {
      await dispatch(fetchArticleAbstract(articleId)).unwrap();
      dispatch(
        addNotification({
          message: "Abstract fetched!",
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "Failed to fetch abstract",
          type: "error",
        }),
      );
    }
  };

  return (
    <div className="page">
      <h1>PubMed Search</h1>

      <div className="pubmed-section">
        <h2>Search Articles</h2>
        <form onSubmit={handleSearch} className="search-form">
          <input
            type="text"
            placeholder="Search PubMed articles..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            required
          />
          <button type="submit" disabled={loading}>
            {loading ? "Searching..." : "Search"}
          </button>
        </form>
      </div>

      <div className="pubmed-section">
        <h2>Search by Article IDs</h2>
        <form onSubmit={handleSearchByIds} className="search-form">
          <input
            type="text"
            placeholder="Comma-separated article IDs..."
            value={searchIds}
            onChange={(e) => setSearchIds(e.target.value)}
            required
          />
          <button type="submit" disabled={loading}>
            {loading ? "Fetching..." : "Fetch by IDs"}
          </button>
        </form>
      </div>

      {error && <div className="error-message">{error}</div>}

      {articles.length > 0 && (
        <div className="articles-section">
          <h2>Results ({articles.length})</h2>
          <div className="articles-grid">
            {articles.map((article) => (
              <div key={article.id} className="article-card">
                <h3>{article.title}</h3>
                <p>
                  <strong>Authors:</strong> {article.authors.join(", ")}
                </p>
                <p>
                  <strong>Journal:</strong> {article.journal}
                </p>
                <p>
                  <strong>Published:</strong>{" "}
                  {new Date(article.publishedDate).toLocaleDateString()}
                </p>
                {article.doi && (
                  <p>
                    <strong>DOI:</strong> {article.doi}
                  </p>
                )}

                <div className="article-actions">
                  <button
                    onClick={() => dispatch(setCurrentArticle(article))}
                    className={
                      currentArticle?.id === article.id ? "active" : ""
                    }
                  >
                    {currentArticle?.id === article.id ? "Selected" : "Select"}
                  </button>
                  <button
                    onClick={() => handleFetchAbstract(article.id)}
                    disabled={loading}
                  >
                    Get Abstract
                  </button>
                </div>

                {article.abstract && (
                  <div className="abstract">
                    <h4>Abstract:</h4>
                    <p>{article.abstract}</p>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      )}

      {currentArticle && (
        <div className="current-article">
          <h2>Selected Article</h2>
          <div className="article-details">
            <h3>{currentArticle.title}</h3>
            <p>
              <strong>Authors:</strong> {currentArticle.authors.join(", ")}
            </p>
            <p>
              <strong>Journal:</strong> {currentArticle.journal}
            </p>
            {currentArticle.abstract && (
              <div className="abstract">
                <h4>Abstract:</h4>
                <p>{currentArticle.abstract}</p>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default PubmedSearch;
