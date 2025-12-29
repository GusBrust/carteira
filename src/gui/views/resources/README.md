# 📁 Pasta de Recursos (Imagens)

Esta pasta contém as imagens usadas na interface gráfica.

## 📋 Imagens Necessárias

Coloque as seguintes imagens nesta pasta:

- `dashboard.png` - Ícone do Dashboard
- `transacoes.png` - Ícone de Transações  
- `adicionar.png` - Ícone de Adicionar
- `dividas.png` - Ícone de Dívidas

## 📝 Como Usar

As imagens são referenciadas nos arquivos FXML usando o prefixo `@`:

```xml
<Image url="@resources/dashboard.png" />
```

O `@` indica que o caminho é relativo ao arquivo FXML.

## 📐 Tamanho Recomendado

- Tamanho: 50x50 pixels (ou múltiplos para alta resolução)
- Formato: PNG (com transparência se necessário)
- Resolução: 72-96 DPI

