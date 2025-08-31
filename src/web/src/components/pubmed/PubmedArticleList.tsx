import { useAppSelector } from "~/store/hooks";
import PubmedArticleCard from "./PubmedArticleCard";

const PubmedArticleList = () => {
  const { articles, currentArticle, loading } = useAppSelector(
    (state) => state.pubmed,
  );
  if (articles && articles.length === 0) {
    return <p>No articles</p>;
  }

  return (
    <div className="container">
      <h2 className="text-2xl">Results ({articles.length})</h2>
      <div className="articles-grid">
        {articles.map((article, id) => (
          <PubmedArticleCard id={id} article={article} />
        ))}
      </div>
    </div>
  );
};

export default PubmedArticleList;
