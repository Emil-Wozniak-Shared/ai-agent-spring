# Spring Agent

This project requires Qdrant and OpenAI API-KEY

### OpenAI Api key
To obtain OpenAI [api-keys](https://platform.openai.com/api-keys) webpage

### Qdrant
You can install qdrant locally using 
```bash
# Arch linux
yay -S qdrant-bin
```

## API Usage Examples:

1. Authenticate:
    ```bash
    http POST :8080/api/token <<<'{ "username": "user", "password": "password" }'
    ```

2. Add Documents:
    ```bash
   http POST http://localhost:8080/api/documents Content-Type:application/json 'Authorization:Bearer <TOKEN>' <<< '[
   {
      "text": "This is a sample document about artificial intelligence and machine learning",
      "metadata": {"category": "AI", "author": "John Doe", "tags": ["ai", "technology"]}
   },
   {
      "text": "Machine learning enables computers to learn without explicit programming",
      "metadata": {"category": "ML", "author": "Jane Smith", "tags": ["ml", "programming"]}
   },
   {
   "text": "Deep learning is a subset of machine learning using neural networks",
   "metadata": {"category": "DL", "author": "Bob Johnson", "tags": ["deep-learning", "neural-networks"]}
   },
   {
      "text": "Natural language processing helps computers understand human language",
   "metadata": {"category": "NLP", "author": "Alice Brown", "tags": ["nlp", "language"]}
   }
   ]'
    ```

3. Search Documents - Basic:
   ```bash
    http POST :8080/api/documents/search Content-Type:application/json 'Authorization:Bearer <TOKEN>' <<< '{
    "query": "artificial intelligence",
    "limit": 5,
    "threshold": 0.7
    }'
   ```

4. Search Documents - Machine Learning:
    ```bash
    http POST :8080/api/documents/search Content-Type:application/json 'Authorization:Bearer <TOKEN>' <<< '{
   "query": "machine learning algorithms",
   "limit": 3,
   "threshold": 0.6
   }'
    ```

5. Search Documents - Neural Networks:
   ```bash
    http POST :8080/api/documents/search Content-Type:application/json 'Authorization:Bearer <TOKEN>' <<< '{
   "query": "neural networks and deep learning",
   "limit": 2,
   "threshold": 0.5
   }'
   ```

6. Search Documents - High Threshold (stricter matching):
    ```bash
    http POST :8080/api/documents/search Content-Type:application/json 'Authorization:Bearer <TOKEN>' <<< '{
   "query": "natural language processing",
   "limit": 10,
   "threshold": 0.8
   }'
   ```

## Key Features of Updated Implementation:

✅ **Named Vectors**: Modern Qdrant approach supporting multiple vectors per point
✅ **Configurable Distance Metrics**: Cosine, Dot, Euclid, Manhattan
✅ **HNSW Index Tuning**: Full control over index performance parameters
✅ **Memory vs Disk Storage**: Configurable vector storage location
✅ **Production Ready**: Proper error handling and logging
