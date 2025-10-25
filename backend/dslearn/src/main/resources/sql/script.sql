CREATE TABLE dsa_topic (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    content_markdown TEXT NOT NULL
);

INSERT INTO dsa_topic (name, content_markdown) VALUES
('Array', '# Arrays

Os **Arrays** são estruturas de dados lineares que armazenam um conjunto de elementos do **mesmo tipo** em posições contíguas de memória. Eles permitem o acesso direto a qualquer elemento por meio de seu **índice**, o que os torna extremamente eficientes para leituras e buscas por posição.

---

## Conceito

Um **Array** funciona como uma sequência ordenada de elementos, onde cada posição é identificada por um índice numérico.  
Por exemplo, um array de inteiros pode armazenar `[10, 20, 30, 40]`, onde o índice `0` representa o valor `10`, o índice `1` o valor `20`, e assim por diante.

Em termos práticos, podemos pensar em um array como uma **prateleira de caixas**, onde cada caixa guarda um valor e todas estão alinhadas lado a lado, facilitando o acesso a qualquer uma delas rapidamente.

---

## Estrutura Interna

Cada elemento em um array é armazenado em posições consecutivas de memória.  
Se o primeiro elemento ocupa a posição de memória `x`, e cada elemento ocupa `k` bytes, o próximo elemento estará na posição `x + k`, e o seguinte em `x + 2k`, e assim sucessivamente.

Essa organização permite o **acesso direto (O(1))** a qualquer elemento, já que o endereço pode ser calculado diretamente a partir do índice.

---

![Diagrama ilustrativo de um Array](https://i.postimg.cc/x8fN1yjc/arrays.png)

---

## Operações Comuns

- **Acesso:** Recupera o valor de um elemento a partir do índice.  
- **Atualização:** Modifica o valor armazenado em uma posição específica.  
- **Inserção:** Adiciona um novo elemento (pode exigir deslocar elementos se não houver espaço).  
- **Remoção:** Exclui um elemento e pode exigir o deslocamento dos demais.  
- **Busca:** Encontra um elemento específico (geralmente O(n) se não houver ordenação).  
- **Percorrimento:** Itera por todos os elementos do array.

---

## Implementação (Python)

```python
# Criação de um array (em Python, usamos listas para esse propósito)
array = [10, 20, 30, 40]

# Acesso a elementos pelo índice
print("Primeiro elemento:", array[0])  # Saída: 10

# Atualização de um valor
array[1] = 25
print("Após atualização:", array)  # Saída: [10, 25, 30, 40]

# Inserção de novo elemento
array.append(50)
print("Após inserção:", array)  # Saída: [10, 25, 30, 40, 50]

# Remoção de elemento
array.remove(30)
print("Após remoção:", array)  # Saída: [10, 25, 40, 50]

# Percorrimento do array
for elemento in array:
    print("Elemento:", elemento)
```

---

## Vantagens

- **Acesso rápido (O(1))** a qualquer elemento via índice.  
- **Estrutura simples** e de fácil implementação.  
- **Melhor uso da memória cache**, pois os elementos são contíguos na memória.

---

## Desvantagens

- **Tamanho fixo:** não pode ser alterado após a criação (em linguagens como C).  
- **Inserções e remoções caras (O(n))**, pois exigem deslocamento dos elementos.  
- **Todos os elementos devem ser do mesmo tipo** (em linguagens fortemente tipadas).

---

## Uso e Aplicações

- Armazenar listas de valores fixos, como notas, temperaturas ou IDs.  
- Implementar estruturas mais complexas como **matrizes**, **filas** e **pilhas**.  
- Processamento de dados em **algoritmos de ordenação e busca**.  
- Uso em **sistemas embarcados** onde o tamanho é conhecido e fixo.

---

## Complexidade

| Operação        | Complexidade de Tempo | Complexidade de Espaço |
|------------------|-----------------------|--------------------------|
| Acesso           | O(1)                  | O(n)                     |
| Atualização      | O(1)                  | O(n)                     |
| Inserção         | O(n)                  | O(n)                     |
| Remoção          | O(n)                  | O(n)                     |
| Busca Linear     | O(n)                  | O(n)                     |

---

Em resumo, os **arrays** são fundamentais em Estruturas de Dados, servindo como base para muitas outras abstrações e algoritmos. Seu principal ponto forte é o acesso direto e eficiente, embora tenham limitações de flexibilidade em tamanho e operações de modificação.
'),
('Linked List', '# Linked List (Lista Encadeada)

As **Linked Lists** (ou **Listas Encadeadas**) são estruturas de dados lineares compostas por uma sequência de **nós** conectados entre si por meio de **referências (ponteiros)**. Diferente dos arrays, os elementos não são armazenados de forma contígua na memória, permitindo inserções e remoções eficientes em qualquer posição.

## Conceito

Uma **lista encadeada** funciona como uma cadeia de elementos, onde cada nó armazena dois dados principais:
- **O valor (dados)**
- **Uma referência (ponteiro)** para o próximo nó da lista

O último nó da lista aponta para `None` (ou `NULL` em C), indicando o final da sequência.  
Essa estrutura é ideal quando o tamanho da lista pode variar dinamicamente, pois não requer alocação de blocos de memória contínuos.

**Analogia:** imagine um conjunto de vagões de trem — cada vagão (nó) carrega uma carga (dado) e está ligado ao próximo por um engate (ponteiro).

## Estrutura Interna

Cada **nó (Node)** da lista contém:
- `valor`: o dado armazenado
- `next`: referência para o próximo nó

A **lista em si** mantém uma referência para o **primeiro nó (head)**, e opcionalmente, para o **último nó (tail)** em versões otimizadas.

![Diagrama ilustrativo de uma Linked List](https://i.postimg.cc/vT0gggD7/lista-ligada.png)

## Operações Comuns

- **Inserção:** adicionar um novo nó no início, fim ou meio da lista.  
- **Remoção:** eliminar um nó específico.  
- **Busca:** percorrer a lista até encontrar um valor.  
- **Travessia:** visitar cada nó da lista em sequência.  

## Implementação (Python)

```python
# Implementação simples de uma Linked List em Python

# Classe que representa um nó da lista
class Node:
    def __init__(self, valor):
        self.valor = valor    # Dado armazenado no nó
        self.next = None      # Ponteiro para o próximo nó

# Classe que representa a Linked List
class LinkedList:
    def __init__(self):
        self.head = None  # Início da lista (vazia)

    # Adiciona um elemento ao final da lista
    def append(self, valor):
        novo_no = Node(valor)
        if self.head is None:
            self.head = novo_no
            return
        atual = self.head
        while atual.next:
            atual = atual.next
        atual.next = novo_no

    # Exibe os elementos da lista
    def mostrar(self):
        atual = self.head
        while atual:
            print(atual.valor, end=" -> ")
            atual = atual.next
        print("None")

# Exemplo de uso
lista = LinkedList()
lista.append(10)
lista.append(20)
lista.append(30)
lista.mostrar()  # Saída: 10 -> 20 -> 30 -> None
```

## Vantagens e Desvantagens

**Vantagens:**
- Inserções e remoções são rápidas (O(1)) quando se tem a referência do nó.  
- Não requer memória contígua.  
- Tamanho da lista pode crescer dinamicamente.  

**Desvantagens:**
- Acesso a elementos é sequencial (O(n)), não é possível acessar diretamente por índice.  
- Uso adicional de memória por conta dos ponteiros.  
- Mais complexa de implementar e manipular que arrays.  

## Uso e Aplicações

- Implementação de **pilhas (stacks)** e **filas (queues)**.  
- Gerenciamento de **memória dinâmica**.  
- Estruturas como **hash tables** (listas de colisão) e **gráfos**.  
- Situações em que há **muitas inserções e remoções** durante a execução.  

## Complexidade

| Operação | Tempo Médio | Tempo Pior Caso |
|-----------|--------------|----------------|
| Inserção no início | O(1) | O(1) |
| Inserção no fim | O(n) | O(n) |
| Remoção | O(n) | O(n) |
| Busca | O(n) | O(n) |
| Espaço | O(n) | O(n) |

Em resumo, **Linked Lists** oferecem flexibilidade em cenários dinâmicos, com custo de acesso mais alto em comparação aos arrays.
'),
('Stack', '# Stack (Pilha)

Uma **Stack** (pilha) é uma estrutura de dados linear que segue o princípio **LIFO** (Last In, First Out - Último a Entrar, Primeiro a Sair). Isso significa que o último elemento adicionado à pilha será o primeiro a ser removido, como uma pilha de pratos onde você sempre adiciona e remove pratos do topo.

## Como Funciona

A pilha funciona através de um ponto de acesso único chamado **topo** (top). Todas as operações de inserção e remoção acontecem exclusivamente neste ponto. Imagine uma pilha de livros: você só pode adicionar um novo livro no topo e só pode remover o livro que está no topo. Não é possível acessar ou remover elementos do meio ou da base sem antes remover todos os elementos acima deles.

O conceito de **LIFO** é fundamental para entender o comportamento da pilha. Quando você adiciona elementos sequencialmente (1, 2, 3), o elemento 3 será o primeiro a ser removido, depois o 2, e por último o 1. Esta característica torna as pilhas ideais para cenários onde a ordem de processamento inversa é necessária.

### Estrutura e Componentes

Uma pilha é composta basicamente por:

* **Topo (Top):** Ponteiro ou referência que indica o elemento no topo da pilha
* **Capacidade (Capacity):** Tamanho máximo da pilha (em implementações com array)
* **Tamanho (Size):** Quantidade atual de elementos na pilha
* **Elementos:** Os dados armazenados na estrutura

![Diagrama ilustrativo de uma Stack mostrando operações push e pop](https://i.postimg.cc/8cZJJJsB/pilha.png)

## Operações Comuns

* **Push (Empilhar):** Adiciona um elemento no topo da pilha
* **Pop (Desempilhar):** Remove e retorna o elemento do topo da pilha
* **Peek/Top (Espiar):** Retorna o elemento do topo sem removê-lo
* **isEmpty (Está Vazia):** Verifica se a pilha está vazia
* **isFull (Está Cheia):** Verifica se a pilha atingiu sua capacidade máxima (em implementações com tamanho fixo)
* **Size (Tamanho):** Retorna a quantidade de elementos na pilha

## Implementação (Python)

```python
class Stack:
    def __init__(self):
        """Inicializa uma pilha vazia"""
        self.items = []
    
    def push(self, item):
        """Adiciona um elemento no topo da pilha"""
        self.items.append(item)
    
    def pop(self):
        """Remove e retorna o elemento do topo"""
        if not self.is_empty():
            return self.items.pop()
        raise IndexError("Pop de uma pilha vazia")
    
    def peek(self):
        """Retorna o elemento do topo sem remover"""
        if not self.is_empty():
            return self.items[-1]
        raise IndexError("Peek de uma pilha vazia")
    
    def is_empty(self):
        """Verifica se a pilha está vazia"""
        return len(self.items) == 0
    
    def size(self):
        """Retorna o tamanho da pilha"""
        return len(self.items)

# Exemplo de uso
pilha = Stack()
pilha.push(10)
pilha.push(20)
pilha.push(30)

print(f"Topo: {pilha.peek()}")  # Saída: Topo: 30
print(f"Removido: {pilha.pop()}")  # Saída: Removido: 30
print(f"Tamanho: {pilha.size()}")  # Saída: Tamanho: 2
```

## Vantagens e Desvantagens

**Vantagens:**

* Operações de inserção e remoção muito rápidas: O(1)
* Implementação simples e intuitiva
* Gerenciamento eficiente de memória em chamadas de função
* Ideal para rastreamento de estados e reversão de operações
* Estrutura natural para problemas recursivos

**Desvantagens:**

* Acesso restrito: só é possível acessar o elemento do topo
* Não permite busca eficiente de elementos no meio da pilha
* Tamanho limitado em implementações com array fixo
* Não é adequada quando é necessário acesso aleatório aos elementos

## Uso e Aplicações

* **Gerenciamento de chamadas de função:** A pilha de execução (call stack) armazena informações sobre funções ativas
* **Avaliação de expressões:** Conversão e cálculo de expressões matemáticas (notação infixa para pós-fixa)
* **Undo/Redo em editores:** Cada ação é empilhada e pode ser desfeita na ordem inversa
* **Navegação de histórico:** Botões "voltar" e "avançar" em navegadores web
* **Verificação de balanceamento:** Validação de parênteses, chaves e colchetes em código
* **Algoritmos de backtracking:** Resolução de labirintos, sudoku e problemas de busca
* **Parsing e compiladores:** Análise sintática de linguagens de programação
* **Inversão de dados:** Reverter strings ou sequências

## Complexidade

| Operação   | Tempo   | Espaço   |
|------------|---------|----------|
| Push       | O(1)    | O(n)     |
| Pop        | O(1)    | O(n)     |
| Peek/Top   | O(1)    | O(n)     |
| Search     | O(n)    | O(n)     |

- **Tempo:** Refere-se ao tempo necessário para executar cada operação.
- **Espaço:** Refere-se ao espaço de memória ocupado pela pilha, proporcional ao número de elementos n.

A eficiência das operações principais (push, pop e peek) em tempo constante torna a pilha uma estrutura de dados extremamente útil para diversos cenários onde o acesso sequencial inverso é necessário.'),
('Queue', '# Queue (Fila)

Uma **Queue** (ou **Fila**) é uma estrutura de dados linear que segue o princípio **FIFO (First In, First Out)** — o primeiro elemento inserido é o primeiro a ser removido. Ela é amplamente utilizada em sistemas onde a ordem de chegada dos elementos deve ser preservada, como filas de impressão, requisições em servidores ou buffers de mensagens.

## Como Funciona

Em uma fila, os elementos são inseridos **no final (enqueue)** e removidos **do início (dequeue)**. Isso garante que o primeiro elemento adicionado seja o primeiro processado.  
Podemos imaginar uma fila de pessoas esperando para serem atendidas: quem chega primeiro é atendido primeiro.

### Estrutura Interna

Uma fila pode ser implementada de várias formas:
- Usando **listas** (arrays dinâmicos);
- Usando **ponteiros**, como em **listas ligadas**;
- Através de **estruturas otimizadas**, como `collections.deque` em Python, que oferece operações O(1) para inserção e remoção.

![Diagrama ilustrativo de uma fila FIFO](https://i.postimg.cc/Hn6cccj8/fila.png)

## Operações Comuns

- **Enqueue (inserção):** adiciona um elemento ao final da fila.
- **Dequeue (remoção):** remove o elemento do início da fila.
- **Front (ou Peek):** retorna o primeiro elemento sem removê-lo.
- **IsEmpty:** verifica se a fila está vazia.
- **Size:** retorna o número de elementos na fila.

## Implementação (Python)

A seguir, um exemplo simples de implementação de uma fila usando `collections.deque`:

```python
from collections import deque

class Queue:
    def __init__(self):
        self.items = deque()

    def is_empty(self):
        # Retorna True se a fila estiver vazia
        return len(self.items) == 0

    def enqueue(self, item):
        # Adiciona um elemento ao final da fila
        self.items.append(item)

    def dequeue(self):
        # Remove e retorna o elemento do início da fila
        if not self.is_empty():
            return self.items.popleft()
        return None

    def peek(self):
        # Retorna o primeiro elemento sem removê-lo
        if not self.is_empty():
            return self.items[0]
        return None

    def size(self):
        # Retorna o número de elementos na fila
        return len(self.items)


# Exemplo de uso
fila = Queue()
fila.enqueue("A")
fila.enqueue("B")
fila.enqueue("C")

print("Primeiro da fila:", fila.peek())  # A
print("Removendo:", fila.dequeue())      # A
print("Novo primeiro:", fila.peek())     # B
```

## Vantagens e Desvantagens

**Vantagens:**
- Mantém a ordem de chegada dos elementos.
- Fácil de implementar e compreender.
- Útil em sistemas de processamento sequencial.

**Desvantagens:**
- Acesso restrito (somente início e fim).
- Pode exigir realocação de memória se implementada com arrays simples.

## Uso e Aplicações

- **Sistemas de atendimento** (filas de espera, impressoras).
- **Processamento de tarefas em background** (job queues).
- **Estruturas de controle em algoritmos BFS (Breadth-First Search)**.
- **Buffers de comunicação** entre processos ou threads.

## Complexidade

| Operação  | Tempo Médio | Descrição |
|------------|--------------|------------|
| Enqueue    | O(1)         | Inserção no final da fila |
| Dequeue    | O(1)         | Remoção no início da fila |
| Peek       | O(1)         | Acesso ao primeiro elemento |
| IsEmpty    | O(1)         | Verificação de estado |
| Size       | O(1)         | Retorna o tamanho da fila |
| Espaço     | O(n)         | Armazena n elementos |
'),
('Tree', '# Tree (Árvore)

Uma **Tree** (ou **Árvore**) é uma estrutura de dados hierárquica composta por **nós (nodes)**, onde cada nó armazena um valor e referências para outros nós chamados de **filhos (children)**. Ela é amplamente usada para representar relações hierárquicas, como sistemas de arquivos, estruturas de diretórios e árvores de decisão.

## Conceito

A **árvore** segue uma estrutura onde existe um **nó raiz (root)** no topo, que se ramifica em **subárvores** menores. Cada nó pode ter **zero ou mais filhos**, e um nó sem filhos é chamado de **folha (leaf)**.  
Não existem ciclos — ou seja, não há caminhos que levem de volta a um nó anterior.

Uma boa analogia é a **árvore genealógica**: um ancestral (raiz) possui filhos, que por sua vez podem ter seus próprios filhos, e assim por diante.

### Estrutura da Árvore

- **Root (raiz):** primeiro nó da árvore.
- **Parent (pai):** nó que possui filhos.
- **Child (filho):** nó descendente de outro nó.
- **Leaf (folha):** nó sem filhos.
- **Edge (aresta):** conexão entre dois nós.
- **Height (altura):** comprimento do maior caminho da raiz até uma folha.

![Diagrama ilustrativo de uma árvore binária](https://i.postimg.cc/2y0bbb3H/tree.png)

## Tipos Comuns de Árvores

- **Árvore Binária:** cada nó tem no máximo dois filhos (esquerdo e direito).
- **Árvore Binária de Busca (BST):** os nós à esquerda contêm valores menores e à direita, maiores.
- **Árvore Balanceada:** mantém altura próxima entre subárvores (ex: AVL, Red-Black).
- **Árvore Genérica:** nós podem ter qualquer número de filhos.
- **Árvore Trie:** usada para armazenar strings, comum em autocomplete e dicionários.

## Operações Comuns

- **Inserção:** adiciona um novo nó à árvore.
- **Busca:** procura um elemento específico.
- **Remoção:** exclui um nó mantendo a estrutura da árvore.
- **Travessias (Traversals):**
  - **Pre-order:** processa o nó antes dos filhos.
  - **In-order:** processa o nó entre os filhos (esquerda → raiz → direita).
  - **Post-order:** processa o nó após os filhos.
  - **Level-order:** processa nós por nível (usando fila).

## Implementação (Python)

A seguir, um exemplo simples de implementação de uma **Árvore Binária de Busca (BST)**:

```python
class Node:
    def __init__(self, value):
        self.value = value
        self.left = None
        self.right = None

class BinarySearchTree:
    def __init__(self):
        self.root = None

    def insert(self, value):
        # Insere um novo valor na árvore
        if self.root is None:
            self.root = Node(value)
        else:
            self._insert(self.root, value)

    def _insert(self, current, value):
        if value < current.value:
            if current.left is None:
                current.left = Node(value)
            else:
                self._insert(current.left, value)
        elif value > current.value:
            if current.right is None:
                current.right = Node(value)
            else:
                self._insert(current.right, value)

    def search(self, value):
        # Busca um valor na árvore
        return self._search(self.root, value)

    def _search(self, current, value):
        if current is None or current.value == value:
            return current
        if value < current.value:
            return self._search(current.left, value)
        return self._search(current.right, value)

    def inorder(self):
        # Travessia em ordem (in-order)
        self._inorder(self.root)

    def _inorder(self, node):
        if node:
            self._inorder(node.left)
            print(node.value, end=" ")
            self._inorder(node.right)

# Exemplo de uso
bst = BinarySearchTree()
bst.insert(8)
bst.insert(3)
bst.insert(10)
bst.insert(1)
bst.insert(6)

print("Travessia em ordem (in-order):")
bst.inorder()  # Saída: 1 3 6 8 10
```

## Vantagens e Desvantagens

**Vantagens:**
- Estrutura eficiente para **busca, inserção e remoção**.
- Facilita **organização hierárquica de dados**.
- Pode ser balanceada para desempenho otimizado.

**Desvantagens:**
- Pode se tornar **desequilibrada**, afetando a performance (em casos degenerados, vira uma lista).
- **Implementação mais complexa** que estruturas lineares.

## Uso e Aplicações

- **Árvores de decisão** em Machine Learning.
- **Índices de bancos de dados** (B-Trees, AVL, Red-Black Trees).
- **Sistemas de arquivos e diretórios**.
- **Compiladores** (análise sintática em árvores de sintaxe).
- **Busca em grafos** (DFS e BFS em árvores).

## Complexidade

| Operação  | Tempo Médio | Pior Caso | Descrição |
|------------|--------------|------------|------------|
| Busca      | O(log n)     | O(n)       | Encontrar um valor na árvore |
| Inserção   | O(log n)     | O(n)       | Inserir novo nó |
| Remoção    | O(log n)     | O(n)       | Remover nó mantendo estrutura |
| Travessia  | O(n)         | O(n)       | Visitar todos os nós |
| Espaço     | O(n)         | O(n)       | Armazena n nós |
'),
('Binary Search Tree', '# Binary Search Tree (Árvore Binária de Busca - BST)

Uma **Binary Search Tree (BST)** é uma estrutura de dados em forma de **árvore binária** na qual cada nó possui **no máximo dois filhos**, e segue uma propriedade fundamental:  
**para qualquer nó, todos os valores da subárvore esquerda são menores que o valor do nó, e todos os valores da subárvore direita são maiores.**

## Conceito

A BST é usada principalmente para **armazenar dados ordenados** de forma que operações como **busca, inserção e remoção** possam ser realizadas de maneira eficiente — geralmente em **O(log n)** tempo em árvores balanceadas.  
Ela combina a estrutura hierárquica das árvores com a lógica de ordenação das listas ordenadas.

Uma boa analogia é uma **lista telefônica organizada por nomes**: se você está procurando “Maria”, você pode ignorar todos os nomes que vêm depois de “Maria” ao olhar para a esquerda, e todos que vêm antes ao olhar para a direita.

### Estrutura Interna

Cada nó da BST contém:
- **Valor (key):** o dado armazenado.
- **Left (esquerda):** ponteiro para o nó filho com valor menor.
- **Right (direita):** ponteiro para o nó filho com valor maior.

![Diagrama ilustrativo de uma Binary Search Tree](https://i.postimg.cc/Kj0kkk4j/bst.png)

## Operações Comuns

- **Inserção:** adiciona um novo nó na posição correta mantendo a ordem.
- **Busca:** localiza um valor seguindo a propriedade da BST.
- **Remoção:** exclui um nó e reorganiza a árvore mantendo a estrutura válida.
- **Travessias:** percorre a árvore em diferentes ordens (pre, in, post, level).

## Implementação (Python)

Abaixo um exemplo funcional de uma **Binary Search Tree** simples:

```python
class Node:
    def __init__(self, value):
        self.value = value
        self.left = None
        self.right = None

class BinarySearchTree:
    def __init__(self):
        self.root = None

    def insert(self, value):
        # Insere um novo valor na BST
        if self.root is None:
            self.root = Node(value)
        else:
            self._insert(self.root, value)

    def _insert(self, node, value):
        if value < node.value:
            if node.left is None:
                node.left = Node(value)
            else:
                self._insert(node.left, value)
        elif value > node.value:
            if node.right is None:
                node.right = Node(value)
            else:
                self._insert(node.right, value)

    def search(self, value):
        # Busca um valor na árvore
        return self._search(self.root, value)

    def _search(self, node, value):
        if node is None or node.value == value:
            return node
        if value < node.value:
            return self._search(node.left, value)
        return self._search(node.right, value)

    def inorder(self):
        # Travessia em ordem crescente
        self._inorder(self.root)

    def _inorder(self, node):
        if node:
            self._inorder(node.left)
            print(node.value, end=" ")
            self._inorder(node.right)

# Exemplo de uso
bst = BinarySearchTree()
for v in [8, 3, 10, 1, 6, 9, 14]:
    bst.insert(v)

print("Travessia em ordem:")
bst.inorder()  # Saída: 1 3 6 8 9 10 14

# Busca de elemento
print("\nExiste o valor 6?", bst.search(6) is not None)
```

## Vantagens e Desvantagens

**Vantagens:**
- Busca, inserção e remoção eficientes (em média O(log n)).
- Mantém os dados ordenados naturalmente.
- Permite travessias que retornam dados em **ordem crescente**.

**Desvantagens:**
- Pode **se tornar desbalanceada**, resultando em desempenho O(n).
- Necessita de **reorganização** (balanceamento) para manter eficiência.
- Implementação mais complexa que listas ou arrays.

## Uso e Aplicações

- **Índices de bancos de dados.**
- **Sistemas de ordenação dinâmica.**
- **Árvores de busca em compiladores e linguagens.**
- **Algoritmos de autocompletar ou sugestões baseadas em prefixos.**
- **Estruturas derivadas como AVL e Red-Black Trees.**

## Complexidade

| Operação  | Tempo Médio | Pior Caso | Descrição |
|------------|--------------|------------|------------|
| Busca      | O(log n)     | O(n)       | Localizar valor |
| Inserção   | O(log n)     | O(n)       | Inserir novo nó |
| Remoção    | O(log n)     | O(n)       | Remover mantendo estrutura |
| Travessia  | O(n)         | O(n)       | Percorrer todos os nós |
| Espaço     | O(n)         | O(n)       | Armazenar n nós |
'),
('Hash Table', '# Hash Tables

Uma **Hash Table** (ou Tabela Hash) é uma estrutura de dados que armazena pares de **chave-valor**, permitindo acesso extremamente rápido aos dados através de uma função hash. Ela mapeia chaves para posições específicas em um array, oferecendo busca, inserção e remoção em tempo médio constante O(1).

## Como Funciona

O funcionamento de uma Hash Table baseia-se em uma **função hash**, que é um algoritmo que transforma uma chave (que pode ser uma string, número ou qualquer tipo de dado) em um **índice numérico** dentro de um array subjacente. Este índice determina onde o valor correspondente será armazenado.

Quando você deseja armazenar um par chave-valor, a função hash processa a chave e gera um índice. O valor é então inserido nesta posição do array. Para recuperar o valor, a mesma função hash é aplicada à chave, gerando o mesmo índice, permitindo acesso direto ao valor armazenado.

**Analogia:** Imagine uma biblioteca onde cada livro tem um código único. Em vez de procurar livro por livro, você usa uma fórmula matemática que converte o código em um número de prateleira específico, levando você diretamente ao livro desejado.

### Colisões

Um desafio importante nas Hash Tables são as **colisões**, que ocorrem quando duas chaves diferentes geram o mesmo índice após passar pela função hash. Existem duas estratégias principais para resolver colisões:

**Encadeamento (Chaining):** Cada posição do array contém uma lista ligada. Quando ocorre uma colisão, o novo elemento é adicionado à lista naquela posição.

**Endereçamento Aberto (Open Addressing):** Quando uma colisão ocorre, o algoritmo procura a próxima posição disponível no array seguindo uma sequência de sondagem (linear, quadrática ou duplo hash).

![Diagrama ilustrativo de uma Hash Table com encadeamento](https://i.postimg.cc/vT0gggDW/hash-map.png)

## Operações Comuns

- **Inserção (put/set):** Aplica a função hash à chave para determinar o índice e armazena o par chave-valor naquela posição
- **Busca (get):** Usa a função hash para localizar o índice da chave e retorna o valor associado
- **Remoção (delete/remove):** Localiza a chave através da função hash e remove o par chave-valor da estrutura
- **Atualização:** Substitui o valor de uma chave existente aplicando a função hash para localizar a posição
- **Verificação de existência (contains/has):** Verifica se uma determinada chave existe na tabela

## Implementação (Python)

```python
class HashTable:
    def __init__(self, tamanho=10):
        # Inicializa um array de listas para encadeamento
        self.tamanho = tamanho
        self.tabela = [[] for _ in range(tamanho)]
    
    def _funcao_hash(self, chave):
        # Função hash simples usando módulo
        return hash(chave) % self.tamanho
    
    def inserir(self, chave, valor):
        # Calcula o índice usando a função hash
        indice = self._funcao_hash(chave)
        
        # Verifica se a chave já existe e atualiza
        for i, (k, v) in enumerate(self.tabela[indice]):
            if k == chave:
                self.tabela[indice][i] = (chave, valor)
                return
        
        # Se não existe, adiciona novo par chave-valor
        self.tabela[indice].append((chave, valor))
    
    def buscar(self, chave):
        # Calcula o índice
        indice = self._funcao_hash(chave)
        
        # Procura a chave na lista encadeada
        for k, v in self.tabela[indice]:
            if k == chave:
                return v
        
        # Retorna None se não encontrar
        return None
    
    def remover(self, chave):
        # Calcula o índice
        indice = self._funcao_hash(chave)
        
        # Remove o par chave-valor se encontrado
        for i, (k, v) in enumerate(self.tabela[indice]):
            if k == chave:
                del self.tabela[indice][i]
                return True
        
        return False

# Exemplo de uso
tabela = HashTable()
tabela.inserir("nome", "João")
tabela.inserir("idade", 25)
tabela.inserir("cidade", "São Paulo")

print(tabela.buscar("nome"))  # Saída: João
print(tabela.buscar("idade"))  # Saída: 25

tabela.remover("idade")
print(tabela.buscar("idade"))  # Saída: None
```

## Vantagens e Desvantagens

**Vantagens:**

- Acesso extremamente rápido aos dados com complexidade O(1) no caso médio
- Inserção e remoção eficientes
- Ideal para implementar caches, índices de banco de dados e dicionários
- Flexibilidade para armazenar diferentes tipos de chaves

**Desvantagens:**

- Desempenho degrada para O(n) no pior caso quando há muitas colisões
- Consumo de memória pode ser alto devido ao array subjacente
- Não mantém ordem de inserção (em implementações básicas)
- Necessidade de uma boa função hash para distribuição uniforme
- Redimensionamento pode ser custoso quando a tabela fica cheia

## Uso e Aplicações

Hash Tables são amplamente utilizadas em diversos cenários:

- **Bancos de dados:** Índices para acelerar consultas e buscas
- **Caches:** Armazenamento temporário de resultados computacionais (memoização)
- **Compiladores:** Tabelas de símbolos para armazenar variáveis e funções
- **Roteadores de rede:** Tabelas de roteamento para encaminhamento rápido de pacotes
- **Detecção de duplicatas:** Verificar rapidamente se um elemento já foi processado
- **Contadores de frequência:** Contar ocorrências de elementos em conjuntos de dados
- **Implementação de Sets e Maps:** Estruturas fundamentais em linguagens de programação
- **Autenticação:** Armazenamento de senhas com hash criptográfico
- **Sistemas de gerenciamento de sessões:** Identificação rápida de sessões de usuários

## Complexidade

**Complexidade de Tempo:**

- **Busca:** O(1) caso médio, O(n) pior caso (quando todas as chaves colidem)
- **Inserção:** O(1) caso médio, O(n) pior caso
- **Remoção:** O(1) caso médio, O(n) pior caso

**Complexidade de Espaço:**

- **O(n):** Onde n é o número de pares chave-valor armazenados

O desempenho de uma Hash Table depende fortemente da qualidade da função hash e do fator de carga (relação entre número de elementos e tamanho do array). Para manter o desempenho O(1), é comum redimensionar a tabela quando o fator de carga ultrapassa um limite (geralmente 0.7 ou 70%).'),
('Graph', '# Grafos

Um **Grafo** é uma estrutura de dados não-linear composta por um conjunto de **vértices** (ou nós) conectados por **arestas** (ou arcos). Os grafos são fundamentais para modelar relações entre objetos, permitindo representar conexões complexas como redes sociais, mapas de rotas, circuitos elétricos e dependências entre tarefas.

## Como Funciona

Um grafo é formalmente definido como G = (V, E), onde V é o conjunto de **vértices** e E é o conjunto de **arestas**. Cada aresta conecta dois vértices, representando algum tipo de relacionamento ou conexão entre eles.

Os grafos podem ser **direcionados** (digrafos), onde as arestas têm uma direção específica (A → B), ou **não-direcionados**, onde as conexões são bidirecionais (A ↔ B). Além disso, as arestas podem ter **pesos** associados, representando custos, distâncias ou capacidades.

**Analogia:** Pense em um grafo como um mapa de uma cidade. Os vértices são os cruzamentos ou pontos de interesse, e as arestas são as ruas que os conectam. Se as ruas têm direção única, temos um grafo direcionado; se podemos ir em ambas as direções, é não-direcionado. As distâncias das ruas representam os pesos das arestas.

### Tipos de Grafos

**Grafo Simples:** Não possui laços (arestas que conectam um vértice a si mesmo) nem arestas múltiplas entre os mesmos vértices.

**Grafo Completo:** Cada vértice está conectado a todos os outros vértices.

**Grafo Bipartido:** Os vértices podem ser divididos em dois conjuntos disjuntos, onde as arestas só conectam vértices de conjuntos diferentes.

**Árvore:** Um grafo conectado e acíclico (sem ciclos).

![Diagrama ilustrativo de diferentes tipos de grafos](https://i.postimg.cc/sxTQQQ1Z/grafo.png)

## Operações Comuns

- **Adição de vértice:** Insere um novo nó no grafo
- **Adição de aresta:** Cria uma conexão entre dois vértices existentes
- **Remoção de vértice:** Remove um nó e todas as arestas conectadas a ele
- **Remoção de aresta:** Elimina uma conexão específica entre dois vértices
- **Busca de vértice:** Verifica se um determinado vértice existe no grafo
- **Busca de aresta:** Verifica se existe conexão entre dois vértices específicos
- **Obtenção de vizinhos:** Retorna todos os vértices adjacentes a um determinado vértice
- **Verificação de conectividade:** Determina se dois vértices estão conectados direta ou indiretamente

## Implementação (Python)

```python
class Grafo:
    def __init__(self, direcionado=False):
        # Dicionário para armazenar a lista de adjacência
        self.vertices = {}
        self.direcionado = direcionado
    
    def adicionar_vertice(self, vertice):
        # Adiciona um novo vértice se não existir
        if vertice not in self.vertices:
            self.vertices[vertice] = []
    
    def adicionar_aresta(self, origem, destino, peso=1):
        # Garante que ambos os vértices existam
        self.adicionar_vertice(origem)
        self.adicionar_vertice(destino)
        
        # Adiciona a aresta
        self.vertices[origem].append((destino, peso))
        
        # Se não é direcionado, adiciona a aresta reversa
        if not self.direcionado:
            self.vertices[destino].append((origem, peso))
    
    def remover_vertice(self, vertice):
        # Remove o vértice e todas as arestas conectadas
        if vertice in self.vertices:
            # Remove arestas que apontam para este vértice
            for v in self.vertices:
                self.vertices[v] = [
                    (destino, peso) for destino, peso in self.vertices[v] 
                    if destino != vertice
                ]
            # Remove o vértice
            del self.vertices[vertice]
    
    def remover_aresta(self, origem, destino):
        # Remove aresta específica
        if origem in self.vertices:
            self.vertices[origem] = [
                (d, p) for d, p in self.vertices[origem] if d != destino
            ]
        
        # Se não é direcionado, remove também a aresta reversa
        if not self.direcionado and destino in self.vertices:
            self.vertices[destino] = [
                (d, p) for d, p in self.vertices[destino] if d != origem
            ]
    
    def exibir_grafo(self):
        # Exibe a representação do grafo
        for vertice in self.vertices:
            vizinhos = [f"{dest}({peso})" for dest, peso in self.vertices[vertice]]
            print(f"{vertice} -> {'', ''.join(vizinhos) if vizinhos else ''sem conexões''}")

# Exemplo de uso
grafo = Grafo(direcionado=False)

# Adicionando vértices e arestas
grafo.adicionar_aresta("A", "B", 5)
grafo.adicionar_aresta("A", "C", 3)
grafo.adicionar_aresta("B", "D", 2)
grafo.adicionar_aresta("C", "D", 4)
grafo.adicionar_aresta("D", "E", 1)

print("Estrutura do grafo:")
grafo.exibir_grafo()
```

## Vantagens e Desvantagens

**Vantagens:**

- Modelagem natural de relacionamentos complexos entre entidades
- Flexibilidade para representar diversos tipos de conexões e estruturas
- Algoritmos poderosos disponíveis para análise e traversal
- Adequado para resolver problemas de otimização como menor caminho
- Escalabilidade para representar redes muito grandes

**Desvantagens:**

- Complexidade computacional alta para algumas operações em grafos grandes
- Consumo de memória pode ser significativo em grafos densos
- Implementação mais complexa comparada a estruturas lineares
- Algoritmos podem ter alta complexidade de tempo dependendo do problema
- Dificuldade de visualização em grafos grandes ou muito conectados

## Uso e Aplicações

Grafos são utilizados em uma ampla variedade de aplicações:

- **Redes sociais:** Modelar conexões entre usuários, análise de influência e detecção de comunidades
- **Sistemas de navegação:** Encontrar rotas mais curtas em mapas (GPS, aplicativos de trânsito)
- **Redes de computadores:** Roteamento de dados, topologia de rede e detecção de falhas
- **Compiladores:** Análise de dependências entre módulos e otimização de código
- **Bioinformática:** Análise de redes de proteínas, sequenciamento de DNA e árvores filogenéticas
- **Sistemas de recomendação:** Relacionar usuários e produtos baseado em preferências
- **Jogos:** Representar mapas, sistemas de movimento e árvores de decisão de IA
- **Logística:** Otimização de rotas de entrega, planejamento de supply chain
- **Análise de dependências:** Gerenciamento de pacotes de software, cronogramas de projetos
- **Detecção de fraudes:** Análise de padrões suspeitos em transações financeiras

## Complexidade

**Representação por Lista de Adjacência:**

- **Espaço:** O(V + E) onde V é o número de vértices e E o número de arestas
- **Adição de vértice:** O(1)
- **Adição de aresta:** O(1)
- **Verificação de aresta:** O(grau do vértice)
- **Remoção de vértice:** O(V + E)

**Representação por Matriz de Adjacência:**

- **Espaço:** O(V²)
- **Adição de vértice:** O(V²) - necessita redimensionar a matriz
- **Adição de aresta:** O(1)
- **Verificação de aresta:** O(1)
- **Remoção de vértice:** O(V²)

A escolha da representação (lista vs matriz de adjacência) depende da densidade do grafo e das operações mais frequentes. Listas de adjacência são preferíveis para grafos esparsos, enquanto matrizes são melhores para grafos densos quando verificações de aresta são frequentes.'),
('Heap', '# Heap

Um **Heap** é uma estrutura de dados baseada em árvore binária que satisfaz a propriedade do heap: em um **Max Heap**, cada nó pai possui valor maior ou igual aos seus filhos; em um **Min Heap**, cada nó pai possui valor menor ou igual aos seus filhos. Heaps são fundamentais para implementar filas de prioridade e algoritmos de ordenação eficientes como o Heap Sort.

## Como Funciona

O Heap é uma **árvore binária completa**, o que significa que todos os níveis estão completamente preenchidos, exceto possivelmente o último, que é preenchido da esquerda para a direita. Esta característica permite que heaps sejam implementados eficientemente usando arrays, onde para um elemento no índice `i`, o filho esquerdo está em `2i + 1` e o filho direito em `2i + 2`, enquanto o pai está em `(i - 1) / 2`.

A **propriedade do heap** garante que o elemento de maior prioridade (máximo ou mínimo) esteja sempre na raiz da árvore, permitindo acesso em tempo constante. Quando elementos são inseridos ou removidos, a estrutura realiza operações de **heapify** para restaurar a propriedade do heap, movendo elementos para cima (bubble up) ou para baixo (bubble down) conforme necessário.

### Tipos de Heap

**Min Heap:** O valor de cada nó é menor ou igual aos valores de seus filhos. A raiz contém o menor elemento.

**Max Heap:** O valor de cada nó é maior ou igual aos valores de seus filhos. A raiz contém o maior elemento.

![Diagrama ilustrativo de um Min Heap e Max Heap](https://i.postimg.cc/fy8tttkc/heaps.png)

## Operações Comuns

- **Inserção (Push):** Adiciona um novo elemento ao final do array e executa bubble up para manter a propriedade do heap
- **Remoção do Topo (Pop):** Remove o elemento da raiz, substitui pela última folha e executa bubble down
- **Peek:** Retorna o elemento de maior prioridade (raiz) sem removê-lo
- **Heapify:** Converte um array desordenado em um heap válido
- **Increase/Decrease Key:** Modifica o valor de um elemento e restaura a propriedade do heap
- **Merge:** Combina dois heaps em um único heap

## Implementação (Python)

```python
class MinHeap:
    def __init__(self):
        self.heap = []
    
    def parent(self, i):
        """Retorna o índice do pai"""
        return (i - 1) // 2
    
    def left_child(self, i):
        """Retorna o índice do filho esquerdo"""
        return 2 * i + 1
    
    def right_child(self, i):
        """Retorna o índice do filho direito"""
        return 2 * i + 2
    
    def swap(self, i, j):
        """Troca dois elementos no heap"""
        self.heap[i], self.heap[j] = self.heap[j], self.heap[i]
    
    def insert(self, valor):
        """Insere um novo valor no heap"""
        self.heap.append(valor)
        self._bubble_up(len(self.heap) - 1)
    
    def _bubble_up(self, i):
        """Move o elemento para cima até satisfazer a propriedade do heap"""
        while i > 0 and self.heap[i] < self.heap[self.parent(i)]:
            self.swap(i, self.parent(i))
            i = self.parent(i)
    
    def extract_min(self):
        """Remove e retorna o menor elemento (raiz)"""
        if len(self.heap) == 0:
            return None
        
        if len(self.heap) == 1:
            return self.heap.pop()
        
        # Guarda o mínimo e substitui pela última folha
        min_val = self.heap[0]
        self.heap[0] = self.heap.pop()
        self._bubble_down(0)
        
        return min_val
    
    def _bubble_down(self, i):
        """Move o elemento para baixo até satisfazer a propriedade do heap"""
        min_index = i
        
        while True:
            left = self.left_child(i)
            right = self.right_child(i)
            
            # Encontra o menor entre pai, filho esquerdo e direito
            if left < len(self.heap) and self.heap[left] < self.heap[min_index]:
                min_index = left
            
            if right < len(self.heap) and self.heap[right] < self.heap[min_index]:
                min_index = right
            
            # Se o pai já é o menor, termina
            if min_index == i:
                break
            
            self.swap(i, min_index)
            i = min_index
    
    def peek(self):
        """Retorna o menor elemento sem removê-lo"""
        return self.heap[0] if self.heap else None
    
    def size(self):
        """Retorna o tamanho do heap"""
        return len(self.heap)

# Exemplo de uso
heap = MinHeap()
heap.insert(10)
heap.insert(5)
heap.insert(20)
heap.insert(1)

print(heap.peek())         # Saída: 1
print(heap.extract_min())  # Saída: 1
print(heap.extract_min())  # Saída: 5
```

## Vantagens e Desvantagens

**Vantagens:**

- Acesso ao elemento de maior prioridade em tempo constante O(1)
- Inserção e remoção eficientes em O(log n)
- Implementação simples usando arrays, sem necessidade de ponteiros
- Uso eficiente de memória devido à representação em array
- Ideal para implementar filas de prioridade
- Boa localidade de cache por usar arrays contíguos

**Desvantagens:**

- Busca de elementos arbitrários é lenta O(n)
- Não mantém os elementos completamente ordenados
- Não suporta busca eficiente de elementos específicos
- Remoção de elementos do meio é custosa O(n)
- Merge de heaps pode ser ineficiente comparado a outras estruturas como Fibonacci Heap

## Uso e Aplicações

- **Filas de Prioridade:** Gerenciamento de tarefas com diferentes prioridades em sistemas operacionais
- **Algoritmo de Dijkstra:** Encontrar o caminho mais curto em grafos
- **Heap Sort:** Algoritmo de ordenação in-place com complexidade O(n log n)
- **Algoritmo de Huffman:** Compressão de dados através de codificação
- **Merge K Sorted Lists:** Combinar múltiplas listas ordenadas eficientemente
- **Mediana em Stream de Dados:** Calcular mediana de dados em tempo real usando dois heaps
- **Escalonamento de Processos:** Sistemas operacionais usam heaps para gerenciar processos
- **Algoritmo de Prim:** Encontrar árvore geradora mínima em grafos
- **Event-Driven Simulation:** Gerenciar eventos ordenados por timestamp

## Complexidade

**Complexidade de Tempo:**

- **Peek (acesso à raiz):** O(1) - acesso direto ao primeiro elemento
- **Inserção:** O(log n) - pode precisar subir até a raiz
- **Remoção do topo:** O(log n) - pode precisar descer até as folhas
- **Heapify (construção):** O(n) - constrói heap a partir de array
- **Busca:** O(n) - necessita percorrer toda a estrutura
- **Decrease/Increase Key:** O(log n) - ajuste e bubble up/down

**Complexidade de Espaço:**

- **Armazenamento:** O(n) - usa array para armazenar n elementos
- **Operações:** O(1) - operações são realizadas in-place, sem memória adicional significativa'),

('Prefix Sums', '# Prefix Sums

Prefix Sums (Somas de Prefixo) é uma técnica de pré-processamento que permite calcular rapidamente a soma de elementos em qualquer intervalo de um array. Em vez de recalcular somas repetidamente, criamos um array auxiliar onde cada posição armazena a soma acumulada de todos os elementos anteriores, transformando consultas de intervalo que normalmente levariam O(n) em operações O(1).

## Como Funciona

O conceito fundamental do Prefix Sums é construir um **array de somas acumuladas** a partir de um array original. Para cada índice `i`, o array de prefix sums armazena a soma de todos os elementos desde o início do array até a posição `i`.

A ideia é simples: se sabemos a soma total até a posição `j` e a soma total até a posição `i-1`, podemos obter a soma do intervalo `[i, j]` simplesmente subtraindo essas duas somas acumuladas. Isso elimina a necessidade de iterar pelo intervalo a cada consulta.

### Construção do Array de Prefix Sums

Dado um array original `arr[0...n-1]`, criamos um array `prefix[0...n]` onde:
- `prefix[0] = 0` (convenção para facilitar cálculos)
- `prefix[i] = prefix[i-1] + arr[i-1]` para `i >= 1`

Dessa forma, `prefix[i]` representa a soma de todos os elementos de `arr[0]` até `arr[i-1]`.

### Calculando a Soma de um Intervalo

Para calcular a soma dos elementos no intervalo `[L, R]` (inclusivo) do array original:

**Soma(L, R) = prefix[R+1] - prefix[L]**

Esta operação é executada em **tempo constante O(1)**, independentemente do tamanho do intervalo.

![Diagrama ilustrativo de Prefix Sums mostrando array original e array acumulado](https://i.postimg.cc/Hn6cccj3/prefix-sum.png)

## Operações Comuns

- **Construção do Array de Prefix Sums:** Percorre o array original uma vez, calculando as somas acumuladas. Complexidade: O(n)
- **Consulta de Soma em Intervalo:** Retorna a soma de elementos entre dois índices usando subtração de valores pré-calculados. Complexidade: O(1)
- **Atualização de Elementos:** Se o array original for modificado, o array de prefix sums precisa ser reconstruído ou atualizado. Complexidade: O(n) para reconstrução completa
- **Múltiplas Consultas:** Ideal para cenários onde há muitas consultas de soma em diferentes intervalos do mesmo array

## Implementação (Python)

```python
class PrefixSum:
    def __init__(self, arr):
        """
        Constrói o array de prefix sums a partir do array original.
        
        Args:
            arr: Lista de números
        """
        self.n = len(arr)
        # Array de prefix sums com tamanho n+1
        self.prefix = [0] * (self.n + 1)
        
        # Construir o array de somas acumuladas
        for i in range(self.n):
            self.prefix[i + 1] = self.prefix[i] + arr[i]
    
    def soma_intervalo(self, L, R):
        """
        Retorna a soma dos elementos no intervalo [L, R] (inclusivo).
        
        Args:
            L: Índice inicial (0-indexed)
            R: Índice final (0-indexed)
        
        Returns:
            Soma dos elementos no intervalo
        """
        if L < 0 or R >= self.n or L > R:
            raise ValueError("Intervalo inválido")
        
        return self.prefix[R + 1] - self.prefix[L]


# Exemplo de uso
arr = [3, 1, 4, 1, 5, 9, 2, 6]
ps = PrefixSum(arr)

# Consultar soma em diferentes intervalos
print(f"Soma [0, 3]: {ps.soma_intervalo(0, 3)}")  # 3+1+4+1 = 9
print(f"Soma [2, 5]: {ps.soma_intervalo(2, 5)}")  # 4+1+5+9 = 19
print(f"Soma [4, 7]: {ps.soma_intervalo(4, 7)}")  # 5+9+2+6 = 22

# Implementação alternativa simplificada
def construir_prefix_sum(arr):
    """Versão simplificada que retorna apenas o array de prefix sums."""
    prefix = [0]
    for num in arr:
        prefix.append(prefix[-1] + num)
    return prefix

# Uso simplificado
arr = [2, 4, 6, 8, 10]
prefix = construir_prefix_sum(arr)
# Soma do intervalo [1, 3]: arr[1] + arr[2] + arr[3] = 4 + 6 + 8 = 18
soma = prefix[4] - prefix[1]
print(f"Soma [1, 3]: {soma}")  # 18
```

## Vantagens e Desvantagens

**Vantagens:**
- Consultas de soma em intervalo extremamente rápidas (O(1))
- Simples de implementar e entender
- Baixo overhead de memória (apenas O(n) adicional)
- Ideal para arrays estáticos ou com poucas atualizações
- Pode ser estendido para 2D (matrizes) e outras variações

**Desvantagens:**
- Ineficiente para arrays que são frequentemente modificados (requer reconstrução O(n))
- Não otimizado para outras operações além de soma (mínimo, máximo, etc.)
- Para arrays dinâmicos com muitas atualizações, estruturas como Segment Tree ou Fenwick Tree são mais adequadas
- Requer espaço adicional proporcional ao tamanho do array

## Uso e Aplicações

- **Problemas de Programação Competitiva:** Resolução eficiente de problemas envolvendo consultas de soma em subarray
- **Análise de Séries Temporais:** Cálculo rápido de médias móveis e agregações em janelas de tempo
- **Processamento de Imagens:** Cálculo de somas em regiões retangulares de imagens (usando Prefix Sum 2D)
- **Estatísticas e Analytics:** Agregação rápida de métricas em diferentes períodos ou segmentos
- **Algoritmos de Otimização:** Base para diversos algoritmos como Kadane''s'' Algorithm modificado
- **Detecção de Padrões:** Identificação de subarrays com soma específica ou propriedades matemáticas
- **Sistemas de Cache:** Pré-computação de valores agregados para melhorar performance de consultas

## Complexidade

**Complexidade de Tempo:**
- **Construção:** O(n) - percorre o array original uma vez para construir o array de prefix sums
- **Consulta de Soma em Intervalo:** O(1) - apenas uma subtração entre dois valores pré-calculados
- **Atualização de Elemento:** O(n) - requer reconstrução do array de prefix sums (se necessário)
- **Múltiplas Consultas:** O(q) para q consultas, cada uma em O(1)

**Complexidade de Espaço:**
- **Espaço Auxiliar:** O(n) - array adicional para armazenar as somas acumuladas
- **Espaço Total:** O(n) - considerando o array original e o array de prefix sums

A técnica é especialmente vantajosa quando o número de consultas é muito maior que o número de atualizações, resultando em complexidade amortizada muito eficiente comparada à abordagem ingênua de somar elementos a cada consulta.'),
('Dynamic Programming', '# Programação Dinâmica

A **Programação Dinâmica** (PD) é uma técnica de otimização utilizada para resolver problemas complexos quebrando-os em subproblemas menores e resolvendo cada subproblema apenas uma vez, armazenando seus resultados para evitar cálculos repetidos. É especialmente eficaz para problemas que possuem subproblemas sobrepostos e estrutura ótima de subproblemas.

## Como Funciona

A programação dinâmica baseia-se em dois princípios principais: **subproblemas sobrepostos** (um mesmo subproblema é resolvido múltiplas vezes) e **otimalidade de subestrutura** (a solução ótima do problema principal pode ser construída a partir das soluções ótimas dos subproblemas). Ao guardar os resultados intermediários em uma tabela (usando técnica chamada de **memoização** ou **tabulação**), evita-se cálculo redundante.

Uma analogia comum é pensar em um viajante que já conhece os melhores caminhos entre duas cidades e utiliza esse conhecimento em viagens futuras, poupando tempo e esforço.

### Componentes de Programação Dinâmica

- **Definição do subproblema:** Identifique e escreva recursivamente o problema original em termos de subproblemas menores.
- **Tabela de memória:** Estrutura (como array ou dicionário) onde os resultados intermediários são armazenados.
- **Ordem de resolução:** Decida se o problema será resolvido de forma top-down (memoização, geralmente usando recursão) ou bottom-up (tabulação, geralmente usando loops).

## Exemplo Visual

![Tabela de programação dinâmica](https://i.postimg.cc/d35kkkDy/dp.png)

## Operações Comuns

- Identificação dos subproblemas
- Resolução recursiva ou iterativa utilizando tabela de armazenamento
- Recuperação da solução ótima na tabela
- Reconstrução do caminho ou das decisões, se necessário

## Implementação (Python)

```python
# Exemplo: Fibonacci com Programação Dinâmica (Tabulação)
def fibonacci(n):
    # Array para armazenar os valores calculados
    dp = [0, 1] + [0]*(n-1)
    for i in range(2, n+1):
        dp[i] = dp[i-1] + dp[i-2]
    return dp[n]

# Testando a função
print(fibonacci(7))  # Saída: 13
```

## Vantagens e Desvantagens

**Vantagens:**
- Evita cálculos repetitivos, tornando algoritmos recursivos viáveis para grandes entradas
- Produz soluções ótimas para problemas de otimização
- Fácil de implementar para muitos problemas clássicos

**Desvantagens:**
- Consome mais memória devido ao armazenamento de resultados intermediários
- Exige análise cuidadosa para identificar corretamente subproblemas e estrutura ótima

## Uso e Aplicações

- Cálculo da sequência de Fibonacci
- Problema da Mochila (Knapsack)
- Problemas de corte de hastes
- Alinhamento de sequências (Bioinformática)
- Edit Distance (Processamento de Texto)
- Cadeias de Markov e probabilidades

## Complexidade

A complexidade de tempo e espaço depende do número de subproblemas e de como eles são armazenados.

- Tipicamente, o tempo é O(n*k), onde n representa o tamanho da entrada e k, parâmetros associados ao problema.
- O uso de tabelas de armazenamento costuma ser O(n) ou O(n*m), dependendo do número de dimensões dos subproblemas.'),
('Two Pointers', '# Dois Ponteiros

A técnica dos **Dois Ponteiros** é uma abordagem eficiente para resolver problemas que envolvem arrays ou listas, onde dois índices percorrem a estrutura simultaneamente para buscar, comparar ou rearranjar elementos. Seu principal objetivo é reduzir a complexidade temporal evitando o uso de loops aninhados.

## Como Funciona

A técnica dos dois ponteiros utiliza duas variáveis indicadoras que percorrem o array ou lista em direções específicas. Geralmente, esses ponteiros se movem a partir de extremos opostos ou de posições iniciais diferentes, dependendo do problema. Conforme avançam, eles processam ou comparam elementos para atingir uma condição desejada.

Essa técnica é especialmente útil para problemas que envolvem o processamento de sequências ordenadas ou que necessitam verificar pares de elementos, removendo duplicatas, rearranjando ou somando valores que satisfaçam algum critério.

Uma analogia simples é pensar em dois corredores em uma pista que começam em pontos diferentes e avançam até se encontrarem ou completarem a corrida, colaborando para cumprir uma tarefa conjunta.

### Etapas Chave da Técnica dos Dois Ponteiros

- Inicialização dos dois ponteiros em posições relevantes (ex: início e fim da lista).
- Movimentação controlada dos ponteiros conforme regras do problema (incrementar, decrementar ou avançar conforme condição).
- Avaliação dos elementos em cada posição para tomada de decisão.
- Possível modificação da estrutura de dados ou acumulação do resultado.

## Exemplo Visual

![Exemplo visual para Two Pointers](https://i.postimg.cc/rsf000zn/two-pointers.png)

## Operações Comuns

- Encontrar pares em um array que somam um valor alvo.
- Remover elementos duplicados ou indesejados em listas ordenadas.
- Verificar se uma string ou array possui propriedades específicas (ex: palíndromo).
- Mover elementos para uma posição específica (ex: zeros para o fim da lista).
- Merge de arrays ordenados.


## Implementação (Python)

```python
# Exemplo: encontrar se existe um par em um array ordenado que soma um valor alvo

def existe_par_com_soma(arr, alvo):
    esquerda, direita = 0, len(arr) - 1
    while esquerda < direita:
        soma = arr[esquerda] + arr[direita]
        if soma == alvo:
            return True  # Par encontrado
        elif soma < alvo:
            esquerda += 1  # Mover ponteiro esquerdo para frente
        else:
            direita -= 1  # Mover ponteiro direito para trás
    return False  # Nenhum par encontrado

# Testando a função
print(existe_par_com_soma([1, 2, 4, 4, 7, 11], 8))  # Saída: True
```


## Vantagens e Desvantagens

**Vantagens:**

- Reduz complexidade de algoritmos que seriam quadráticos para linear.
- Fácil implementação para problemas com dados ordenados.
- Pode ser adaptado para diversos tipos de problemas envolvendo arrays e strings.

**Desvantagens:**

- Funciona melhor com dados ordenados ou que permitam movimentação lógica dos ponteiros.
- Nem sempre aplicável para problemas que não possuem estrutura linear ou condições claras para movimentação dos ponteiros.


## Uso e Aplicações

- Verificação de pares com soma específica.
- Ordenação e merge de arrays.
- Remoção de duplicatas em listas ordenadas.
- Validação de palíndromos em strings.
- Manipulação de estruturas lineares para atender restrições específicas.


## Complexidade

- Tempo: O(n), já que cada ponteiro percorre o array no máximo uma vez.
- Espaço: O(1), pois utiliza apenas variáveis auxiliares para os ponteiros e não aloca estruturas adicionais significativas.

'),
('Sliding Window', '# Sliding Window

A **Sliding Window** (ou *Janela Deslizante*) é uma técnica utilizada para otimizar algoritmos que trabalham com **subconjuntos contínuos de dados**, como listas ou arrays. Ela é amplamente usada em problemas de processamento de sequência, onde é necessário analisar partes do conjunto sem recalcular tudo a cada passo.

---

## Conceito

A técnica **Sliding Window** consiste em manter uma “janela” que se move sobre a estrutura de dados, processando apenas os elementos dentro dessa janela a cada iteração. Isso evita a repetição de cálculos, melhorando significativamente a eficiência.

Imagine que você quer calcular a soma de todos os subarrays de tamanho `k` em um array. Em vez de recalcular toda a soma para cada subarray, você pode **adicionar o próximo elemento** e **remover o primeiro da janela anterior**, economizando tempo.

Essa técnica é comum em problemas de **média móvel**, **busca de subcadeias**, **detecção de padrões**, e **otimização de desempenho** em fluxos contínuos de dados.

---

### Estrutura e Funcionamento

- A janela tem um **tamanho fixo** (k) ou **variável**, dependendo do problema.  
- Em cada passo:
  1. O limite direito da janela avança (inclusão de um novo elemento).
  2. O limite esquerdo pode ser movido (remoção de um elemento antigo).
- O processamento é feito com base apenas nos elementos dentro da janela atual.

---

![Ilustração da técnica Sliding Window em um array](https://i.postimg.cc/2y0bbb39/sliding-window.png)

---

## Etapas do Algoritmo

- **Inicialização:** Defina o tamanho da janela e os índices de início e fim.  
- **Expansão:** Mova o final da janela para incluir novos elementos.  
- **Atualização:** Atualize a métrica (soma, média, máximo etc.) com base na mudança da janela.  
- **Contração (opcional):** Mova o início da janela quando necessário (por exemplo, quando o tamanho excede `k`).  
- **Iteração:** Continue até o final do array.

---

## Implementação (Python)

```python
# Exemplo: Soma máxima de uma subjanela de tamanho k

def max_sum_sliding_window(arr, k):
    # Verifica se o tamanho da janela é válido
    if len(arr) < k or k <= 0:
        return 0

    # Calcula a soma inicial da primeira janela
    window_sum = sum(arr[:k])
    max_sum = window_sum

    # Move a janela da esquerda para a direita
    for i in range(k, len(arr)):
        # Atualiza a soma removendo o primeiro e adicionando o próximo elemento
        window_sum += arr[i] - arr[i - k]
        max_sum = max(max_sum, window_sum)

    return max_sum

# Exemplo de uso
array = [2, 1, 5, 1, 3, 2]
k = 3
print("Maior soma de uma janela de tamanho", k, ":", max_sum_sliding_window(array, k))
```

**Saída esperada:**  
```
Maior soma de uma janela de tamanho 3 : 9
```

---

## Vantagens e Desvantagens

### ✅ Vantagens
- Reduz o custo computacional de O(n*k) para **O(n)** em muitos casos.
- Fácil de implementar e adaptar a diferentes problemas.
- Ideal para dados contínuos e em tempo real.

### ❌ Desvantagens
- Requer que os dados estejam em uma sequência contínua.
- Pode ser menos intuitivo em problemas com janelas de tamanho variável.
- Nem todos os problemas se beneficiam dessa abordagem.

---

## Uso e Aplicações

- **Cálculo de médias móveis** (em séries temporais e sensores).  
- **Análise de substrings** (como busca de padrões em texto).  
- **Problemas de máximo/mínimo em intervalos fixos**.  
- **Monitoramento de desempenho** em sistemas de streaming de dados.  
- **Detecção de anomalias** em janelas de eventos recentes.

---

## Complexidade

- **Tempo:** O(n), pois cada elemento entra e sai da janela no máximo uma vez.  
- **Espaço:** O(1) para janela fixa (ou O(k) para armazenar os elementos).

---

A técnica **Sliding Window** é uma das mais poderosas em otimização de algoritmos que lidam com dados sequenciais, oferecendo eficiência e simplicidade na resolução de problemas práticos.
'),
('Binary Search', '# Binary Search

A **Busca Binária** é um algoritmo eficiente para encontrar um elemento em uma lista **ordenada**. Ela reduz pela metade o espaço de busca a cada iteração, tornando-a muito mais rápida do que a busca linear em listas grandes.

## Conceito

A ideia central da busca binária é dividir o problema repetidamente ao meio. Dado um array ordenado, o algoritmo compara o elemento do meio com o valor procurado:

- Se o valor do meio for igual ao elemento buscado, o algoritmo termina.
- Se o valor do meio for maior, a busca continua na metade esquerda.
- Se o valor do meio for menor, a busca continua na metade direita.

Esse processo se repete até encontrar o elemento ou até o intervalo de busca ficar vazio.

### Estrutura do Algoritmo

O algoritmo depende de três variáveis principais:

- **início**: o índice inicial da busca.
- **fim**: o índice final da busca.
- **meio**: o ponto central entre início e fim.

![Diagrama ilustrativo da Busca Binária](https://i.postimg.cc/KjG38rct/binary-search.png)

## Etapas do Algoritmo

- Definir os índices de início e fim.
- Calcular o índice do meio: `(inicio + fim) // 2`.
- Comparar o valor no meio com o elemento buscado.
- Atualizar o intervalo (início/fim) conforme o resultado da comparação.
- Repetir até encontrar o valor ou o intervalo ser inválido.

## Implementação (Python)

```python
def busca_binaria(lista, alvo):
    inicio = 0
    fim = len(lista) - 1

    while inicio <= fim:
        meio = (inicio + fim) // 2  # Calcula o índice do meio
        if lista[meio] == alvo:
            return meio  # Elemento encontrado, retorna o índice
        elif lista[meio] < alvo:
            inicio = meio + 1  # Busca na metade direita
        else:
            fim = meio - 1  # Busca na metade esquerda

    return -1  # Elemento não encontrado

# Exemplo de uso
dados = [1, 3, 5, 7, 9, 11]
print(busca_binaria(dados, 7))  # Saída: 3
```

## Vantagens e Desvantagens

**Vantagens:**
- Extremamente eficiente em listas grandes.
- Complexidade logarítmica (cresce lentamente com o tamanho dos dados).
- Fácil de implementar tanto de forma iterativa quanto recursiva.

**Desvantagens:**
- Requer que a lista esteja **ordenada**.
- Ineficiente em listas pequenas ou dinâmicas (com inserções/remoções frequentes).

## Uso e Aplicações

- Pesquisa em **bancos de dados ordenados**.
- **Algoritmos de busca** em árvores binárias de busca (BST).
- Busca em **dicionários, catálogos, registros e logs**.
- **Verificação de condições** em algoritmos de otimização (como busca binária em resposta).

## Complexidade

- **Melhor caso:** O(1) — elemento encontrado no primeiro teste.
- **Caso médio:** O(log n).
- **Pior caso:** O(log n).
- **Complexidade espacial:** O(1) na versão iterativa e O(log n) na versão recursiva (por conta da pilha de chamadas).
'),
('Depth-First Search', '# Busca em Profundidade (DFS)

A **Busca em Profundidade (Depth-First Search - DFS)** é um algoritmo fundamental usado para percorrer ou buscar elementos em **grafos** e **árvores**. Ele segue uma estratégia de ir o mais fundo possível em um ramo antes de retroceder, explorando assim caminhos completos antes de explorar outros.

## Como Funciona

A DFS utiliza o princípio de **exploração profunda**: começa em um nó inicial e segue para um de seus vizinhos, depois para o vizinho desse vizinho, e assim por diante, até não haver mais nós a visitar. Quando atinge um ponto sem novos caminhos, o algoritmo **retrocede** (backtrack) para explorar caminhos alternativos ainda não visitados.

Essa abordagem pode ser implementada de duas formas:
- **Recursiva:** utilizando a pilha de chamadas da função.
- **Iterativa:** utilizando uma **pilha (stack)** manualmente.

A DFS é particularmente útil para detectar ciclos, encontrar componentes conectados, resolver labirintos e realizar ordenações topológicas.

### Estrutura Conceitual

A ideia básica é manter uma estrutura de dados (pilha) que armazene os nós a visitar. Cada nó é marcado como visitado para evitar revisitas, e o processo continua até que todos os nós tenham sido explorados.

![Diagrama ilustrativo da DFS explorando um grafo](https://i.postimg.cc/mkmzzzhz/dfs.png)

## Etapas do Algoritmo

1. Escolher um nó inicial.
2. Marcar o nó como **visitado**.
3. Visitar recursivamente todos os **vizinhos não visitados**.
4. Se não houver vizinhos disponíveis, **retroceder** ao nó anterior.
5. Repetir até que todos os nós sejam visitados.

## Implementação (Python)

```python
# Implementação simples de DFS em Python (usando recursão)

# Grafo representado como um dicionário de listas
grafo = {
    ''A'': [''B'', ''C''],
    ''B'': [''D'', ''E''],
    ''C'': [''F''],
    ''D'': [],
    ''E'': [''F''],
    ''F'': []
}

visitados = set()

def dfs(no):
    # Marca o nó como visitado
    print(no)
    visitados.add(no)
    
    # Visita cada vizinho não visitado
    for vizinho in grafo[no]:
        if vizinho not in visitados:
            dfs(vizinho)

# Executa a DFS a partir do nó ''A''
dfs(''A'')
```

**Saída esperada:**
```
A B D E F C
```

## Vantagens e Desvantagens

**Vantagens:**
- Simples de implementar (especialmente de forma recursiva).
- Usa menos memória que a BFS em grafos grandes e esparsos.
- Útil para encontrar caminhos ou detectar ciclos.

**Desvantagens:**
- Pode ficar presa em ciclos se não houver controle de nós visitados.
- Nem sempre encontra o caminho mais curto.

## Uso e Aplicações

- **Detecção de ciclos** em grafos direcionados e não direcionados.
- **Busca em labirintos** e resolução de puzzles.
- **Ordenação topológica** em grafos direcionados acíclicos.
- **Análise de conectividade** em redes.
- **Geração de árvores de profundidade** para IA ou jogos.

## Complexidade

- **Tempo:** O(V + E), onde **V** é o número de vértices e **E** é o número de arestas.
- **Espaço:** O(V), devido à pilha de chamadas recursivas ou estrutura auxiliar.

'),
('Breadth-First Search', '# Busca em Largura (BFS)

A **Busca em Largura** (Breadth-First Search - BFS) é um algoritmo de
**traversal** (percurso) usado para explorar todos os vértices de um
grafo ou árvore **nível por nível**. É amplamente utilizado para
encontrar o **menor caminho** em grafos não ponderados e verificar
**conectividade** entre nós.

## Como Funciona

A BFS começa em um nó inicial (ou raiz) e visita **todos os nós vizinhos
diretamente conectados** antes de avançar para os vizinhos desses nós.
Isso é feito usando uma **fila (queue)**, garantindo que os nós sejam
explorados em ordem de descoberta.

A ideia principal é: **visitar primeiro os nós mais próximos** do ponto
de partida e, depois, os mais distantes.

### Estrutura Interna

A BFS utiliza: - **Fila (queue):** para controlar a ordem de visita dos
nós. - **Conjunto ou lista de visitados:** para evitar revisitar nós. -
**Grafo (ou matriz de adjacência):** para armazenar as conexões entre
nós.

![Diagrama ilustrativo da
BFS](https://i.postimg.cc/prW5LYVz/bfs.png)

## Etapas do Algoritmo

1.  Escolher um nó inicial e marcá-lo como visitado.\
2.  Inserir o nó na **fila**.\
3.  Enquanto a fila não estiver vazia:
    -   Remover o primeiro nó da fila.\
    -   Visitar todos os seus vizinhos não visitados.\
    -   Adicionar esses vizinhos à fila e marcá-los como visitados.

## Implementação (Python)

``` python
from collections import deque

def bfs(grafo, inicio):
    visitados = set()            # Conjunto de nós já visitados
    fila = deque([inicio])       # Fila com o nó inicial
    visitados.add(inicio)

    while fila:
        no_atual = fila.popleft()     # Remove o primeiro nó da fila
        print(no_atual, end=" ")      # Processa o nó atual (ex: imprime)

        # Visita os vizinhos do nó atual
        for vizinho in grafo[no_atual]:
            if vizinho not in visitados:
                visitados.add(vizinho)
                fila.append(vizinho)

# Exemplo de uso
grafo = {
    ''A'': [''B'', ''C''],
    ''B'': [''D'', ''E''],
    ''C'': [''F''],
    ''D'': [],
    ''E'': [''F''],
    ''F'': []
}

bfs(grafo, ''A'')
# Saída esperada: A B C D E F
```

## Vantagens e Desvantagens

**Vantagens:** 
- Garante o **menor caminho** em grafos não ponderados. 
- Simples de implementar. 
- Ideal para **buscas em largura** (nível por
nível).

**Desvantagens:** 
- Pode consumir **muita memória**, pois armazena
múltiplos nós na fila. 
- Não funciona bem em grafos **muito grandes**
sem otimizações.

## Uso e Aplicações

-   Encontrar o **menor caminho** em grafos não ponderados.
-   Verificar **conectividade** entre nós.
-   Resolver **puzzles** (como o Cubo de Rubik ou labirintos).
-   Sistemas de recomendação e **propagação de informação** em redes
    sociais.

## Complexidade

-   **Tempo:** O(V + E), onde V é o número de vértices e E o número de
    arestas.\
-   **Espaço:** O(V), devido à fila e ao conjunto de visitados.
'),
('Backtracking', '# Backtracking

O **Backtracking** é uma técnica de **busca e construção de soluções** utilizada em problemas onde é necessário explorar múltiplas possibilidades, retrocedendo quando um caminho não leva a uma solução válida. É muito usado em problemas combinatórios, de otimização e de decisão, como **sudoku, labirintos, permutações** e **subconjuntos**.

## Como Funciona

O Backtracking funciona de forma **recursiva**, tentando construir uma solução **passo a passo**. A cada passo, o algoritmo escolhe uma opção e avança. Caso essa escolha leve a um caminho inválido, ele **retrocede (backtrack)** para o ponto anterior e tenta outra opção.

É como explorar um labirinto: você segue um caminho até encontrar uma parede; então volta ao último cruzamento e tenta outro caminho. O processo continua até encontrar uma saída ou esgotar todas as possibilidades.

### Estrutura do Processo

1. **Escolha:** selecione uma opção possível.
2. **Restrição:** verifique se a escolha é válida.
3. **Meta:** verifique se a solução foi encontrada.
4. **Backtrack:** caso contrário, volte e tente outra opção.

![Diagrama ilustrativo de Backtracking](https://i.postimg.cc/WzNq16pM/backtracking.png)

## Etapas do Algoritmo

- Escolher uma opção inicial.
- Verificar se a opção atual é válida (respeita as restrições).
- Se for válida, avançar para a próxima etapa.
- Se não for válida, retornar (backtrack) e escolher outra alternativa.
- Continuar até encontrar uma solução completa ou esgotar as possibilidades.

## Implementação (Python)

```python
# Exemplo simples de Backtracking: todas as permutações de uma lista

def permutacoes(nums):
    resultado = []

    def backtrack(caminho, restantes):
        # Caso base: se não há mais elementos, adiciona a permutação completa
        if not restantes:
            resultado.append(caminho[:])
            return

        # Tenta cada opção possível
        for i in range(len(restantes)):
            # Escolhe o elemento atual
            caminho.append(restantes[i])
            # Continua com os elementos restantes
            backtrack(caminho, restantes[:i] + restantes[i+1:])
            # Retrocede (remove o último elemento)
            caminho.pop()

    backtrack([], nums)
    return resultado

# Exemplo de uso
print(permutacoes([1, 2, 3]))
```

## Vantagens e Desvantagens

**Vantagens:**
- Encontra todas as soluções possíveis.
- Fácil de implementar usando recursão.
- Pode ser otimizado com **poda (pruning)** para evitar caminhos inúteis.

**Desvantagens:**
- Pode ser **muito lento** (explora muitas combinações).
- O consumo de memória cresce rapidamente em casos grandes.
- Ineficiente para problemas com grande número de possibilidades.

## Uso e Aplicações

- Resolver **Sudoku** e **labirintos**.
- Gerar **permutação e combinação** de elementos.
- Resolver o **problema das N rainhas**.
- Construir **subconjuntos** e **partições**.
- Planejamento e busca em **inteligência artificial**.

## Complexidade

A complexidade de tempo depende do número de combinações possíveis.  
Em geral, o pior caso é **exponencial**, tipicamente **O(b^d)**, onde:
- **b** é o número médio de escolhas por passo.
- **d** é a profundidade da árvore de recursão.

A complexidade de espaço é **O(d)**, correspondente à profundidade da pilha de recursão.
');

CREATE TABLE exercise (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    url VARCHAR(512),
    difficulty VARCHAR(50),
    related_topics TEXT,
    companies TEXT
);

\COPY exercise(title, difficulty, url, companies, related_topics) FROM 'backend\dslearn\src\main\resources\csv\leetcode.csv' WITH (FORMAT CSV, HEADER);